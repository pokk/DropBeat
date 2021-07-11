/*
 * MIT License
 *
 * Copyright (c) 2021 Jieyi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package taiwan.no.one.mediaplayer.lyric.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.List;
import taiwan.no.one.mediaplayer.lyric.LrcRowEntity;

/**
 * Custom LrcView, can display lyrics, drag lyrics, zoom lyrics
 */
public class LrcView extends View implements ILrcView {

    public final static String TAG = "LrcView";

    /**
     * Normal lyrics mode
     */
    public final static int DISPLAY_MODE_NORMAL = 0;
    /**
     * Drag lyrics mode
     */
    public final static int DISPLAY_MODE_SEEK = 1;
    /**
     * Zoom lyrics mode
     */
    public final static int DISPLAY_MODE_SCALE = 2;
    /**
     * The minimum moving distance, if it is less than this distance when dragging the lyrics, no processing will be
     * done
     */
    private final int mMinSeekFiredOffset = 10;
    /**
     * The font color of the current highlighted lyrics is yellow
     */
    private final int mHignlightRowColor = Color.YELLOW;
    /**
     * The font color of the unhighlighted lyrics is white
     */
    private final int mNormalRowColor = Color.WHITE;
    /**
     * When dragging lyrics, the font color of a straight line below the currently highlighted lyrics
     **/
    private final int mSeekLineColor = Color.CYAN;
    /**
     * When dragging the lyrics, the font color of the time when the currently highlighted lyrics is displayed
     **/
    private final int mSeekLineTextColor = Color.CYAN;
    /**
     * When dragging the lyrics, the minimum font size of the time when the currently highlighted lyrics is displayed
     **/
    private final int mMinSeekLineTextSize = 13;
    /**
     * When dragging the lyrics, the maximum font size of the time when the currently highlighted lyrics is displayed
     **/
    private final int mMaxSeekLineTextSize = 18;
    /**
     * The minimum lyrics font size
     **/
    private final int mMinLrcFontSize = 15;
    /**
     * Maximum lyrics font size
     **/
    private final int mMaxLrcFontSize = 35;
    /**
     * Spacing between two lines of lyrics
     **/
    private final int mPaddingY = 10;
    /**
     * When dragging the lyrics, the starting position of a straight line below the currently highlighted lyrics
     **/
    private final int mSeekLinePaddingX = 0;
    private final Paint mPaint;
    /**
     * The coordinates of the first finger
     **/
    private final PointF mPointerOneLastMotion = new PointF();
    /**
     * The coordinates of the second finger
     **/
    private final PointF mPointerTwoLastMotion = new PointF();
    /**
     * Current display mode of lyrics
     */
    private int mDisplayMode = DISPLAY_MODE_NORMAL;
    /**
     * Lyrics collection, including lyrics of all lines
     */
    private List<LrcRowEntity> mLrcRows;
    /**
     * The number of lines of the currently highlighted lyrics
     */
    private int mHignlightRow = 0;
    /**
     * When dragging the lyrics, the default font size of the time when the currently highlighted lyrics is displayed
     **/
    private int mSeekLineTextSize = 30;
    /**
     * Default lyrics font size
     **/
    private int mLrcFontSize = 50;    // font size of lrc
    /**
     * Drag the listening class of the lyrics, call back the onLrcSeeked method of the LrcViewListener class
     **/
    private ILrcViewListener mLrcViewListener;
    /**
     * The content displayed when there is no lyrics
     **/
    private String mLoadingLrcTip = "Downloading lrc...";
    private float mLastMotionY;
    /**
     * Whether it is the first movement, when a finger is pressed and then starts to move, set to true, When the second
     * finger is pressed, that is, when two fingers are moving at the same time, set to false
     */
    private boolean mIsFirstMove = false;

    public LrcView(Context context, AttributeSet attr) {
        super(context, attr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mLrcFontSize);
    }

    public void setLoadingTipText(String text) {
        mLoadingLrcTip = text;
    }

    /**
     * Set the lyrics to be highlighted as the first few lines of lyrics
     *
     * @param position The number of lines of lyrics to be highlighted
     * @param cb       is the lyrics to be highlighted after the finger drag
     */
    public void seekLrc(int position, boolean cb) {
        if (mLrcRows == null || position < 0 || position > mLrcRows.size()) {
            return;
        }
        LrcRowEntity lrcRow = mLrcRows.get(position);
        mHignlightRow = position;
        invalidate();
        //If you drag the lyrics with your fingers
        if (mLrcViewListener != null && cb) {
            //Call back the onLrcSeeked method to move the position of the music player to the position of the highlighted lyrics
            mLrcViewListener.onLrcSeeked(position, lrcRow);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mLrcRows == null || mLrcRows.size() == 0) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            //Finger press
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "down,mLastMotionY:" + mLastMotionY);
                mLastMotionY = event.getY();
                mIsFirstMove = true;
                invalidate();
                break;
            //Finger move
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 2) {
                    Log.d(TAG, "two move");
                    doScale(event);
                    return true;
                }
                Log.d(TAG, "one move");
                // single pointer mode ,seek
                //If you press two fingers at the same time to zoom in and out of the lyrics, lift up one of your fingers and move the other finger without leaving the screen, do nothing
                if (mDisplayMode == DISPLAY_MODE_SCALE) {
                    //if scaling but pointer become not two ,do nothing.
                    return true;
                }
                //If one finger presses and moves on the screen, drag the lyrics up and down
                doSeek(event);
                break;
            case MotionEvent.ACTION_CANCEL:
                //Finger up
            case MotionEvent.ACTION_UP:
                if (mDisplayMode == DISPLAY_MODE_SEEK) {
                    //Highlight the lyrics when the finger is lifted and play the lyrics from that sentence
                    seekLrc(mHignlightRow, true);
                }
                mDisplayMode = DISPLAY_MODE_NORMAL;
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int height = getHeight(); // height of this view
        final int width = getWidth(); // width of this view
        //When there is no lyrics
        if (mLrcRows == null || mLrcRows.size() == 0) {
            if (mLoadingLrcTip != null) {
                // draw tip when no lrc.
                mPaint.setColor(mHignlightRowColor);
                mPaint.setTextSize(mLrcFontSize);
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(mLoadingLrcTip, width / 2, height / 2 - mLrcFontSize, mPaint);
            }
            return;
        }

        int rowY = 0; // vertical point of each row.
        final int rowX = width / 2;
        int rowNum = 0;
        /**
         * Draw lyrics in the following three steps:
         *
         * Step 1: Highlight the lyrics that is playing
         * Step 2: Draw the lyrics that can be displayed above the lyrics that are being played
         * Step 3: Draw the lyrics that can be displayed below the lyrics being played
         */
        // 1. Highlight the lyrics that are going to be highlighted
        String highlightText = mLrcRows.get(mHignlightRow).getContent();
        int highlightRowY = height / 2 - mLrcFontSize;
        mPaint.setColor(mHignlightRowColor);
        mPaint.setTextSize(mLrcFontSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(highlightText, rowX, highlightRowY, mPaint);

        // When dragging the lyrics up and down, draw the time of the lyric to be highlighted and a straight line under the lyric to be highlighted
        if (mDisplayMode == DISPLAY_MODE_SEEK) {
            // Draw a straight line under the highlighted lyrics
            mPaint.setColor(mSeekLineColor);
            //The x coordinate of the line is from 0 to the width of the screen, and the y coordinate is between the highlighted lyrics and the next line of lyrics
            canvas.drawLine(mSeekLinePaddingX,
                            highlightRowY + mPaddingY,
                            width - mSeekLinePaddingX,
                            highlightRowY + mPaddingY,
                            mPaint);

            // Draw the time for the highlighted lyrics
            mPaint.setColor(mSeekLineTextColor);
            mPaint.setTextSize(mSeekLineTextSize);
            mPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(mLrcRows.get(mHignlightRow).getStrTime(), 0, highlightRowY, mPaint);
        }

        // 2. Draw the lyrics that can be displayed above the lyrics that are being played
        mPaint.setColor(mNormalRowColor);
        mPaint.setTextSize(mLrcFontSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        rowNum = mHignlightRow - 1;
        rowY = highlightRowY - mPaddingY - mLrcFontSize;
        //Only draw the last lyrics of the lyrics being played
//        if (rowY > -mLrcFontSize && rowNum >= 0) {
//            String text = mLrcRows.get(rowNum).content;
//            canvas.drawText(text, rowX, rowY, mPaint);
//        }

        //Draw all the lyrics above the lyrics being played
        while (rowY > -mLrcFontSize && rowNum >= 0) {
            String text = mLrcRows.get(rowNum).getContent();
            canvas.drawText(text, rowX, rowY, mPaint);
            rowY -= (mPaddingY + mLrcFontSize);
            rowNum--;
        }

        // 3. Draw the lyrics that can be displayed below the lyrics being played
        rowNum = mHignlightRow + 1;
        rowY = highlightRowY + mPaddingY + mLrcFontSize;

        //Only draw the next lyrics of the lyrics being played
//        if (rowY < height && rowNum < mLrcRows.size()) {
//            String text2 = mLrcRows.get(rowNum).content;
//            canvas.drawText(text2, rowX, rowY, mPaint);
//        }

        //Draw all the lyrics that can be displayed below the lyrics that are playing
        while (rowY < height && rowNum < mLrcRows.size()) {
            String text = mLrcRows.get(rowNum).getContent();
            canvas.drawText(text, rowX, rowY, mPaint);
            rowY += (mPaddingY + mLrcFontSize);
            rowNum++;
        }

    }

    /**
     * Deal with the lyrics size scaling when two fingers move on the screen
     */
    private void doScale(MotionEvent event) {
        //If the mode of the lyrics is: drag the lyrics mode
        if (mDisplayMode == DISPLAY_MODE_SEEK) {
            //If you press with one finger, scroll up and down in the lyrics, and then press another finger to change the lyrics mode from dragging lyrics mode to zooming lyrics mode
            mDisplayMode = DISPLAY_MODE_SCALE;
            Log.d(TAG, "change mode from DISPLAY_MODE_SEEK to DISPLAY_MODE_SCALE");
            return;
        }
        // two pointer mode , scale font
        if (mIsFirstMove) {
            mDisplayMode = DISPLAY_MODE_SCALE;
            invalidate();
            mIsFirstMove = false;
            //The x and y coordinates of the two fingers
            setTwoPointerLocation(event);
        }
        //Get the scale of lyrics size
        int scaleSize = getScale(event);
        Log.d(TAG, "scaleSize:" + scaleSize);
        //If the zoom size is not equal to 0, zoom and redraw the LrcView
        if (scaleSize != 0) {
            setNewFontSize(scaleSize);
            invalidate();
        }
        setTwoPointerLocation(event);
    }

    /**
     * Handling the lyrics scroll up and down when the single finger moves on the screen
     */
    private void doSeek(MotionEvent event) {
        float y = event.getY();//The y coordinate of the current position of the finger
        float offsetY = y - mLastMotionY; //The difference between the y coordinate of the first press and the y coordinate of the current moving finger position
        //If the moving distance is less than 10, nothing will be done
        if (Math.abs(offsetY) < mMinSeekFiredOffset) {
            return;
        }
        //Set the mode to drag lyrics mode
        mDisplayMode = DISPLAY_MODE_SEEK;
        int rowOffset = Math.abs((int) offsetY / mLrcFontSize); //The number of lines to scroll the lyrics

        Log.d(TAG, "move to new hightlightrow : " + mHignlightRow + " offsetY: " + offsetY + " rowOffset:" + rowOffset);

        if (offsetY < 0) {
            //Move your finger up, the lyrics scroll down
            mHignlightRow += rowOffset;//Set the lyrics to be highlighted as the currently highlighted lyrics, scroll down the lyrics after rowOffset row
        }
        else if (offsetY > 0) {
            //Move your finger down, the lyrics scroll up
            mHignlightRow -= rowOffset;//Set the lyrics to be highlighted as the currently highlighted lyrics, scroll up the lyrics after the rowOffset line
        }
        //Set the lyrics to be highlighted to the larger value of 0 and mHignlightRow, that is, if mHignlightRow <0, mHignlightRow=0
        mHignlightRow = Math.max(0, mHignlightRow);
        //Set the lyrics to be highlighted to the smaller value of 0 and mHignlightRow, that is, if mHignlight> RowmLrcRows.size()-1, mHignlightRow=mLrcRows.size()-1
        mHignlightRow = Math.min(mHignlightRow, mLrcRows.size() - 1);
        //If the number of lines to be scrolled by the lyrics is greater than 0, redraw LrcView
        if (rowOffset > 0) {
            mLastMotionY = y;
            invalidate();
        }
    }

    /**
     * Set the x and y coordinates of the current two fingers
     */
    private void setTwoPointerLocation(MotionEvent event) {
        mPointerOneLastMotion.x = event.getX(0);
        mPointerOneLastMotion.y = event.getY(0);
        mPointerTwoLastMotion.x = event.getX(1);
        mPointerTwoLastMotion.y = event.getY(1);
    }

    /**
     * Set the font size after scaling
     */
    private void setNewFontSize(int scaleSize) {
        //Set the latest font size after scaling lyrics
        mLrcFontSize += scaleSize;
        mLrcFontSize = Math.max(mLrcFontSize, mMinLrcFontSize);
        mLrcFontSize = Math.min(mLrcFontSize, mMaxLrcFontSize);

        //Set the latest font size of lyrics
        mSeekLineTextSize += scaleSize;
        mSeekLineTextSize = Math.max(mSeekLineTextSize, mMinSeekLineTextSize);
        mSeekLineTextSize = Math.min(mSeekLineTextSize, mMaxSeekLineTextSize);
    }

    /**
     * Get the scale of lyrics size
     */
    private int getScale(MotionEvent event) {
        Log.d(TAG, "scaleSize getScale");
        float x0 = event.getX(0);
        float y0 = event.getY(0);
        float x1 = event.getX(1);
        float y1 = event.getY(1);

        float maxOffset = 0; // max offset between x or y axis,used to decide scale size

        boolean zoomin = false;
        //The gap in the x coordinate between the first two fingers
        float oldXOffset = Math.abs(mPointerOneLastMotion.x - mPointerTwoLastMotion.x);
        //The difference in the x coordinate between the second two fingers
        float newXoffset = Math.abs(x1 - x0);

        //The y coordinate gap between the first two fingers
        float oldYOffset = Math.abs(mPointerOneLastMotion.y - mPointerTwoLastMotion.y);
        //The y coordinate gap between the second two fingers
        float newYoffset = Math.abs(y1 - y0);

        //After two fingers move, judge the biggest difference between the two fingers
        maxOffset = Math.max(Math.abs(newXoffset - oldXOffset), Math.abs(newYoffset - oldYOffset));
        //If the x coordinate moves more
        if (maxOffset == Math.abs(newXoffset - oldXOffset)) {
            //If the difference between the x-coordinates of the second two fingers is greater than the difference of the x-coordinates between the first two fingers, it is zoomed in, otherwise it is reduced
            zoomin = newXoffset > oldXOffset;
        }
        //If the y coordinate moves more
        else {
            //If the y-coordinate gap between the second two fingers is greater than the y-coordinate gap between the first two fingers, it is zoomed in, otherwise it is reduced
            zoomin = newYoffset > oldYOffset;
        }
        Log.d(TAG, "scaleSize maxOffset:" + maxOffset);
        if (zoomin) {
            return (int) (maxOffset / 10);//Enlarge 1/10 of the largest gap between two fingers
        }
        else {
            return -(int) (maxOffset / 10);//Reduce 1/10 of the largest gap between the movement of two fingers
        }
    }

    /**
     * Set lyrics line collection
     *
     * @param lrcRows
     */
    public void setLrc(List<LrcRowEntity> lrcRows) {
        mLrcRows = lrcRows;
        invalidate();
    }

    /**
     * When playing, call this method to scroll the lyrics, highlight the lyrics being played
     *
     * @param time
     */
    public void seekLrcToTime(long time) {
        if (mLrcRows == null || mLrcRows.size() == 0) {
            return;
        }
        if (mDisplayMode != DISPLAY_MODE_NORMAL) {
            return;
        }
        Log.d(TAG, "seekLrcToTime:" + time);

        for (int i = 0 ; i < mLrcRows.size() ; i++) {
            LrcRowEntity current = mLrcRows.get(i);
            LrcRowEntity next = i + 1 == mLrcRows.size() ? null : mLrcRows.get(i + 1);
            /**
             * The playing time is greater than the time of the lyrics of the current line but less than the time of the lyrics of the next line, set the highlight line of the current line
             * When the playing time is greater than the lyrics of the current line, and the current line is the last line of lyrics, set the highlight line of the current line
             */
            if ((time >= current.getTime() && next != null && time < next.getTime()) || (time > current.getTime() && next == null)) {
                seekLrc(i, false);
                return;
            }
        }
    }

    public void setListener(ILrcViewListener l) {
        mLrcViewListener = l;
    }
}
