/**
 * TableCellBox.java
 * Copyright (c) 2005-2007 Radek Burget
 *
 * CSSBox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * CSSBox is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public License
 * along with CSSBox. If not, see <http://www.gnu.org/licenses/>.
 *
 * Created on 29.9.2006, 14:15:23 by burgetr
 */
package org.cordovastudio.editors.designer.rendering.engines.cssBox.layout;

import cz.vutbr.web.css.CSSProperty;
import cz.vutbr.web.css.TermLengthOrPercent;
import org.cordovastudio.editors.designer.rendering.engines.cssBox.css.HTMLNorm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import java.awt.*;

/**
 * A box that represents a single table cell.
 * @author burgetr
 */
public class TableCellBox extends BlockBox
{
    private static Logger log = LoggerFactory.getLogger(TableCellBox.class);

    protected int colspan;
    protected int rowspan;
    
    /** first row of this cell in the body */
    protected int row;
    /** first collumn of this cell in the row */
    protected int column; 
    
    /** the owner row */
    protected TableRowBox ownerRow;
    /** the owner column */
    protected TableColumn ownerColumn;
    
    /** relative width [%] when used */
    protected int percent;
    
    /** vertical content offset produced by vertical alignment */
    protected int coffset;
    
    //====================================================================================
    
    /**
     * Create a new table cell
     */
    public TableCellBox(Element n, Graphics2D g, VisualContext ctx)
    {
        super(n, g, ctx);
        isblock = true;
        loadAttributes();
        fleft = new FloatList(this);
        fright = new FloatList(this);
        coffset = 0;
    }

    /**
     * Create a new table cell from an inline box
     */
    public TableCellBox(InlineBox src)
    {
        super(src);
        isblock = true;
        loadAttributes();
        fleft = new FloatList(this);
        fright = new FloatList(this);
        coffset = 0;
    }

    /**
     * @return the column span
     */
    public int getColspan()
    {
        return colspan;
    }

    /**
     * @return the row span
     */
    public int getRowspan()
    {
        return rowspan;
    }

    public void setColspan(int colspan)
    {
        this.colspan = colspan;
    }

    public void setRowspan(int rowspan)
    {
        this.rowspan = rowspan;
    }

    /**
     * @return the row of this cell in the table body
     */
    public int getRow()
    {
        return row;
    }

    /**
     * @return the column of this cell in the row
     */
    public int getColumn()
    {
        return column;
    }

    /**
	 * @return the ownerRow
	 */
	public TableRowBox getOwnerRow()
	{
		return ownerRow;
	}

	/**
	 * @param ownerRow the ownerRow to set
	 */
	public void setOwnerRow(TableRowBox ownerRow)
	{
		this.ownerRow = ownerRow;
	}

	/**
	 * @return the ownerColumn
	 */
	public TableColumn getOwnerColumn()
	{
		return ownerColumn;
	}

	/**
	 * @param ownerColumn the ownerColumn to set
	 */
	public void setOwnerColumn(TableColumn ownerColumn)
	{
		this.ownerColumn = ownerColumn;
	}

	/**
     * Set the index of the first row and column of this cell in the row
     * @param column the column index to set
     * @param row the row index to set
     */
    public void setCellPosition(int column, int row)
    {
        this.column = column;
        this.row = row;
    }

    /**
     * Set the total width of the cell. The content width is computed automatically.
     * @param width the width to be set
     */
    public void setWidth(int width)
    {
        content.width = width - border.left - padding.left - padding.right - border.right;
        bounds.width = width;
        wset = true;
        updateChildSizes();
    }

    /**
     * Set the total width of the cell. The content width is computed automatically.
     * @param height the height to be set
     */
    public void setHeight(int height)
    {
        content.height = height - border.top - padding.top - padding.bottom - border.bottom;
        bounds.height = height;
        hset = true;
    }

    /**
     * @return the percentage when the width is specified relatively
     */
    public int getPercent()
    {
        return percent;
    }

    @Override
    public String toString()
    {
        return super.toString() + "[" + column + "," + row + "]";
    }

    //====================================================================================

    /**
     * Applies the vertical alignment after the row height is computed. Moves all child boxes
     * according to the vertical-align value of the cell and the difference between the original
     * height (after the cell layout) and the new (required by the row) height.
     * @param origHeight the original cell height obtained from the content layout
     * @param newHeight the cell height required by the row. It should be greater or equal to origHeight.
     * @param baseline the row's baseline offset
     */
    public void applyVerticalAlign(int origHeight, int newHeight, int baseline)
    {
        int yofs = 0;
        CSSProperty.VerticalAlign valign = style.getProperty("vertical-align");
        if (valign == null) valign = CSSProperty.VerticalAlign.MIDDLE;
        switch (valign)
        {
            case TOP: yofs = 0;
                      break;
            case BOTTOM: yofs = newHeight - origHeight;
                         break;
            case MIDDLE: yofs = (newHeight - origHeight) / 2;
                         break;
            default: yofs = baseline - getFirstInlineBoxBaseline(); //all other values should behave as BASELINE
                     break;
        }

        if (yofs > 0)
            coffset = yofs;
    }

    @Override
    public int getAbsoluteContentY()
    {
        //apply the possible content offset caused by vertical alignment
        return super.getAbsoluteContentY() + coffset;
    }

    @Override
    public void computeEfficientMargins()
    {
        //no margin collapsing with the contents for table cells
        emargin = new LengthSet(margin);
    }

    @Override
    public int getMinimalWidth()
    {
        int ret = getMinimalContentWidth();
        if (!wrelative && hasFixedWidth() && content.width > ret)
            ret = content.width;
        ret += margin.left + padding.left + border.left +
               margin.right + padding.right + border.right;
        return ret;
    }

    @Override
    public int getMaximalWidth()
    {
        int ret = getMaximalContentWidth();
        /*if (!wrelative && hasFixedWidth())
            ret = content.width;*/
        //increase by margin, padding, border
        ret += margin.left + padding.left + border.left +
               margin.right + padding.right + border.right;
        return ret;
    }

    @Override
    protected void loadSizes(boolean update)
    {
        CSSDecoder dec = new CSSDecoder(ctx);

        //containing box sizes
        if (cblock == null)
            { log.debug(toString() + " has no cblock"); return; }
        int contw = cblock.getContentWidth();

        //Borders
        if (!update) //borders needn't be updated
            loadBorders(dec, contw);

        //Padding
        loadPadding(dec, contw);

        //Content and margins
        if (!update)
        {
            content = new Dimension(0, 0);
            margin = new LengthSet();
            emargin = new LengthSet();
            min_size = new Dimension(-1, -1);
            max_size = new Dimension(-1, -1);
            coords = new LengthSet();
        }

        //Load the width if set
        CSSProperty.Width wprop = null;
        TermLengthOrPercent width = null;
        String widthattr = getElement().getAttribute("width"); //try to load from attribute
        if (!widthattr.equals(""))
        {
            width = HTMLNorm.createLengthOrPercent(widthattr);
            if (width != null)
                wprop = width.isPercentage() ? CSSProperty.Width.percentage : CSSProperty.Width.length;
        }
        else //no attribute set - use the style
        {
            wprop = style.getProperty("width");
            width = getLengthValue("width");
        }

        if (wprop == null || wprop == CSSProperty.Width.AUTO)
        {
            wset = false;
        }
        else
        {
            wset = true;
            if (!update)
                content.width = dec.getLength(width, false, 0, 0, contw);
            if (width.isPercentage())
            {
            	wrelative = true;
            	percent = (int) Math.round(width.getValue());
                if (percent == 0)
                    wrelative = false; //consider 0% as absolute 0
            }
        }

        //Load the height if set
        String heightattr = getElement().getAttribute("height"); //try to load from attribute
        CSSProperty.Height hprop = null;
        TermLengthOrPercent height = null;
        if (!heightattr.equals(""))
        {
            height = HTMLNorm.createLengthOrPercent(widthattr);
            if (height != null)
                hprop = height.isPercentage() ? CSSProperty.Height.percentage : CSSProperty.Height.length;
        }
        else
        {
            hprop = style.getProperty("height");
            height = getLengthValue("height");
        }

        boolean hauto = (hprop == null || hprop == CSSProperty.Height.AUTO);
        if (cblock.hset)
        {
            hset = !hauto;
            if (!update)
                content.height = dec.getLength(height, hauto, 0, 0, cblock.getContentHeight());
        }
        else
        {
            hset = (!hauto && !height.isPercentage());
            if (!update)
                content.height = dec.getLength(height, hauto, 0, 0, 0);
        }
    }

	@Override
	public boolean hasFixedWidth()
	{
		//return wset && !isRelative(); //for cells, the width cannot be computed from the containing box now
	    //return false; //we can never be sure for table cells, depends on column widths
		return true; //the width is set by the column
	}

	@Override
	public boolean hasFixedHeight()
	{
		return false;
	}

    @Override
	public boolean canIncreaseWidth()
    {
        return true;
    }

    @Override
    public void drawBackground(Graphics2D g)
    {
        Color color = g.getColor(); //original color

        //top left corner
        int x = absbounds.x;
        int y = absbounds.y;

        //border bounds
        int bx1 = x + margin.left;
        int by1 = y + margin.top;
        int bw = border.left + padding.left + content.width + padding.right + border.right;
        int bh = border.top + padding.top + content.height + padding.bottom + border.bottom;
        int bx2 = bx1 + bw - 1;
        int by2 = by1 + bh - 1;

        //draw the background - it should be visible below the border too
        //if no background is specified for the cell, we try to use the row, row group, column, column group
        Color bg = bgcolor;
        if (bg == null)
        	bg = getOwnerRow().getBgcolor();
        if (bg == null)
        	bg = getOwnerColumn().getBgcolor();
        if (bg == null)
        	bg = getOwnerRow().getOwnerBody().getBgcolor();

        if (bg != null)
        {
            g.setColor(bg);
            g.fillRect(bx1, by1, bw, bh);
        }

        //draw the border
        drawBorders(g, bx1, by1, bx2, by2);

        g.setColor(color); //restore original color
    }

    @Override
    protected boolean separatedFromTop(ElementBox box)
    {
        return true;
    }

    @Override
    protected boolean separatedFromBottom(ElementBox box)
    {
        return true;
    }
    
    @Override
    protected boolean encloseFloats()
    {
        return true; //table cell always encloses contained floats
    }

    /**
     * Loads the important values from the element attributes.
     */
    protected void loadAttributes()
    {
        try {
            if (!el.getAttribute("colspan").equals(""))
                colspan = Integer.parseInt(el.getAttribute("colspan"));
            else
                colspan = 1;
            if (!el.getAttribute("rowspan").equals(""))
                rowspan = Integer.parseInt(el.getAttribute("rowspan"));
            else
                rowspan = 1;
        } catch (NumberFormatException e) {
            log.warn("Invalid width value: " + e.getMessage());
        }
    }
    
}
