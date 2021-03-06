package com.radiance.winddirections.grid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.radiance.winddirections.R

/**
 * View responsible for displaying grid in a wind directions([com.radiance.winddirections.WindDirections])
 *
 * To display grid in a activity, add a grid to the activity's layout XML file:
 *
 *      <com.radiance.winddirections.grid.WindDirectionGrid
 *          android:id="@+id/grid_id"
 *          android:layout_height="wrap_content"
 *          android:layout_width="wrap_content />
 *
 * All XML style attributes available on Grid:
 *
 *      textSize        - type: Dimension (directions title text size)
 *      textColor       - type: Color (direction title text color)
 *      gridSize        - type: Dimension (grid size)
 *      gridColor       - type: Color (grid color)
 *
 * @param context The Context the Grid is running in, through which it can
 *        access the current theme, resources, etc.
 * @param attrs The attributes of the XML Grid tag being used to inflate the view.
 *
 * @property textSize corresponds to textSize attribute.
 * @property textColor corresponds to textColor attribute.
 * @property gridSize corresponds to gridSize attribute.
 * @property gridColor corresponds to gridColor attribute.
 */
class WindDirectionGrid(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val mainPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var circlesNumber: Int = defaultCirclesNumber
        set(value) {
            field = value
            updateView()
        }
    var textSize = defaultTextSize
        set(value) {
            field = value
            updateView()
        }
    var gridSize = defaultGridSize
        set(value) {
            field = value
            updateView()
        }

    private var textLineSize = defaultGridSize

    var gridColor = defaultGridColor
        set(value) {
            field = value
            updateView()
        }
    var textColor = defaultTextColor
        set(value) {
            field = value
            updateView()
        }

    init {
        setAttributes(context, attrs)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawGrid(canvas)
        drawText(canvas)
    }

    private fun drawGrid(canvas: Canvas?) {
        mainPaint.color = gridColor
        mainPaint.style = Paint.Style.STROKE
        mainPaint.strokeWidth = gridSize

        val path = Path()

        val radius = (if (width <= height) width.toFloat() else height.toFloat()) / 2
        val lineSize = radius * 0.9f

        val centerX = width / 2f
        val centerY = height / 2f

        for (i in circlesNumber downTo 0)
            path.addCircle(centerX, centerY, lineSize * (1F / circlesNumber) * i, Path.Direction.CW)

        path.moveTo(centerX - lineSize, centerY)
        path.rLineTo(2 * lineSize, 0F)
        path.moveTo(centerX, centerY - lineSize)
        path.rLineTo(0F, 2 * lineSize)

        canvas?.drawPath(path, mainPaint)
    }

    private fun drawText(canvas: Canvas?) {
        mainPaint.style = Paint.Style.FILL_AND_STROKE
        mainPaint.textSize = textSize
        mainPaint.strokeWidth = textLineSize
        mainPaint.color = textColor

        val radius = (if (width <= height) width.toFloat() else height.toFloat()) / 2

        val xN = width / 2f - textSize / 3f
        val yN = (height / 2f) - radius + textSize
        canvas?.drawText("N", xN, yN, mainPaint)

        val xE = (width / 2f) + radius - textSize
        val yE = (height / 2f) + textSize / 3
        canvas?.drawText("E", xE, yE, mainPaint)

        val xS = width / 2f - textSize / 4f
        val yS = (height / 2f) + radius - (textSize / 5f)
        canvas?.drawText("S", xS, yS, mainPaint)


        val xW = (width / 2f) - radius + (textSize / 5f)
        val yW = (height / 2f) + textSize / 3
        canvas?.drawText("W", xW, yW, mainPaint)
    }

    private fun updateView() {
        invalidate()
        requestLayout()
    }

    private fun setAttributes(context: Context, attrs: AttributeSet) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.WindDirectionGrid, 0, 0
        )
        gridColor =
            typedArray.getColor(R.styleable.WindDirectionGrid_grid_color, defaultGridColor)
        textColor = typedArray.getColor(
            R.styleable.WindDirectionGrid_text_color,
            defaultTextColor
        )
        textSize = typedArray.getDimensionPixelSize(
            R.styleable.WindDirectionGrid_text_size,
            defaultTextSize.toInt()
        ).toFloat()

        gridSize = typedArray.getDimensionPixelSize(
            R.styleable.WindDirectionGrid_grid_size,
            defaultGridSize.toInt()
        ).toFloat()
        circlesNumber = typedArray.getInteger(
            R.styleable.WindDirectionGrid_circles_number,
            defaultCirclesNumber
        )
    }

    companion object {
        var defaultCirclesNumber = 3
        var defaultTextSize = 40F
        var defaultGridSize = 1F
        var defaultGridColor = Color.BLACK
        var defaultTextColor = Color.BLACK
    }

}