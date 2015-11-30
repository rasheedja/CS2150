package Designer;

import org.lwjgl.opengl.GL11;

import GraphicsLab.Colour;
import GraphicsLab.Normal;
import GraphicsLab.Vertex;
import org.lwjgl.util.glu.Cylinder;

/**
 * The shape designer is a utility class which assits you with the design of 
 * a new 3D object. Replace the content of the drawUnitShape() method with
 * your own code to creates vertices and draw the faces of your object.
 * 
 * You can use the following keys to change the view:
 *   - TAB		switch between vertex, wireframe and full polygon modes
 *   - UP		move the shape away from the viewer
 *   - DOWN     move the shape closer to the viewer
 *   - X        rotate the camera around the x-axis (clockwise)
 *   - Y or C   rotate the camera around the y-axis (clockwise)
 *   - Z        rotate the camera around the z-axis (clockwise)
 *   - SHIFT    keep pressed when rotating to spin anti-clockwise
 *   - A 		Toggle colour (only if using submitNextColour() to specify colour)
 *   - SPACE	reset the view to its initial settings
 *  
 * @author Remi Barillec
 *
 */
public class ShapeDesigner extends AbstractDesigner {

	/**
	 * Main method
	 **/
	public static void main(String args[]) {
		new ShapeDesigner().run(WINDOWED, "Designer", 0.01f);
	}

	/**
	 * Draw the shape
	 **/
	protected void drawUnitShape() {

        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
        GL11.glDisable(GL11.GL_LIGHTING);
        Colour.WHITE.submit();
		new Cylinder().draw(10.0f, 10.0f, 50.0f, 10, 10);
	}
}
