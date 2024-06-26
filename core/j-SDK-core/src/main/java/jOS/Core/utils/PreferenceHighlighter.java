/*
 * Copyright (C) 2018 The Android Open Source Project
 * Copyright (C) 2024 ._______166
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jOS.Core.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.TypedArray;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Property;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.preference.Preference;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.State;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

/**
 * Utility class for highlighting a preference
 */
public class PreferenceHighlighter extends ItemDecoration implements Runnable {

    private static final Property<PreferenceHighlighter, Integer> HIGHLIGHT_COLOR =
            new Property<PreferenceHighlighter, Integer>(Integer.TYPE, "highlightColor") {

                @Override
                public Integer get(PreferenceHighlighter highlighter) {
                    return highlighter.mHighlightColor;
                }

                @Override
                public void set(PreferenceHighlighter highlighter, Integer value) {
                    highlighter.mHighlightColor = value;
                    highlighter.mRv.invalidateItemDecorations();
                }
            };

    private static final long HIGHLIGHT_DURATION = 15000L;
    private static final long HIGHLIGHT_FADE_OUT_DURATION = 500L;
    private static final long HIGHLIGHT_FADE_IN_DURATION = 200L;
    private static final int END_COLOR = setColorAlphaBound(Color.WHITE, 0);

    private final Paint mPaint = new Paint();
    private final RecyclerView mRv;
    private final int mIndex;
    private final Preference mPreference;
    private final RectF mDrawRect = new RectF();

    private boolean mHighLightStarted = false;
    private int mHighlightColor = END_COLOR;

    public PreferenceHighlighter(RecyclerView rv, int index, Preference preference) {
        mRv = rv;
        mIndex = index;
        mPreference = preference;
    }

    /**
     * Set the alpha component of {@code color} to be {@code alpha}. Unlike the support lib version,
     * it bounds the alpha in valid range instead of throwing an exception to allow for safer
     * interpolation of color animations
     */
    @ColorInt
    public static int setColorAlphaBound(int color, int alpha) {
        if (alpha < 0) {
            alpha = 0;
        } else if (alpha > 255) {
            alpha = 255;
        }
        return (color & 0x00ffffff) | (alpha << 24);
    }

    public static int getColorAccent(Context context) {
        return getAttrColor(context, android.R.attr.colorAccent);
    }

    /**
     * Returns the color associated with the attribute
     */
    public static int getAttrColor(Context context, int attr) {
        TypedArray ta = context.obtainStyledAttributes(new int[]{attr});
        int colorAccent = ta.getColor(0, 0);
        ta.recycle();
        return colorAccent;
    }

    @Override
    public void run() {
        mRv.addItemDecoration(this);
        mRv.smoothScrollToPosition(mIndex);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, State state) {
        ViewHolder holder = parent.findViewHolderForAdapterPosition(mIndex);
        if (holder == null) {
            return;
        }
        if (!mHighLightStarted && state.getRemainingScrollVertical() != 0) {
            // Wait until scrolling stopped
            return;
        }

        if (!mHighLightStarted) {
            // Start highlight
            int colorTo = setColorAlphaBound(getColorAccent(mRv.getContext()), 66);
            ObjectAnimator anim = ObjectAnimator.ofArgb(this, HIGHLIGHT_COLOR, END_COLOR,
                    colorTo);
            anim.setDuration(HIGHLIGHT_FADE_IN_DURATION);
            anim.setRepeatMode(ValueAnimator.REVERSE);
            anim.setRepeatCount(4);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    removeHighlight();
                }
            });
            anim.start();
            mHighLightStarted = true;
        }

        View view = holder.itemView;
        mPaint.setColor(mHighlightColor);
        mDrawRect.set(0, view.getY(), parent.getWidth(), view.getY() + view.getHeight());
        if (mPreference instanceof HighlightDelegate) {
            ((HighlightDelegate) mPreference).offsetHighlight(view, mDrawRect);
        }
        c.drawRect(mDrawRect, mPaint);
    }

    private void removeHighlight() {
        ObjectAnimator anim = ObjectAnimator.ofArgb(
                this, HIGHLIGHT_COLOR, mHighlightColor, END_COLOR);
        anim.setDuration(HIGHLIGHT_FADE_OUT_DURATION);
        anim.setStartDelay(HIGHLIGHT_DURATION);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRv.removeItemDecoration(PreferenceHighlighter.this);
            }
        });
        anim.start();
    }

    /**
     * Interface to be implemented by a preference to customize the highlight are
     */
    public interface HighlightDelegate {

        /**
         * Allows the preference to update the highlight area
         */
        void offsetHighlight(View prefView, RectF bounds);

    }
}