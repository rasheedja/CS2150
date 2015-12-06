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
		new ShapeDesigner().run(WINDOWED, "Designer", 0.1f);
	}

	/**
	 * Draw the shape
	 **/
	protected void drawUnitShape()
	{
		Vertex v1 = new Vertex(-0.5f, -0.5f, -0.5f); // left,  back,  bottom
		Vertex v2 = new Vertex(0.5f, -0.5f, -0.5f);  // right, back,  bottom
		Vertex v3 = new Vertex(0.5f, -0.5f, 0.5f);   // right, front, bottom
		Vertex v4 = new Vertex(-0.5f, -0.5f, 0.5f);  // left,  front, bottom
		Vertex v5 = new Vertex(-0.3f, 0.5f, 0.5f);   // left,  front, top
		Vertex v6 = new Vertex(-0.3f, 0.5f, -0.5f);  // left,  back,  top
		Vertex v7 = new Vertex(0.3f, 0.5f, -0.5f); // right, back,  top
		Vertex v8 = new Vertex(0.3f, 0.5f, 0.5f); // right, front, top

		// draw the near face
		Colour.WHITE.submit();
		GL11.glBegin(GL11.GL_POLYGON);
		{
			v5.submit();
			v4.submit();
			v3.submit();
			v8.submit();
		}
		GL11.glEnd();

		// draw the far face
        Colour.WHITE.submit();
		GL11.glBegin(GL11.GL_POLYGON);
		{
			v6.submit();
			v7.submit();
			v2.submit();
			v1.submit();
		}
        GL11.glEnd();

		// draw the top face
        Colour.WHITE.submit();
		GL11.glBegin(GL11.GL_POLYGON);
		{
			v7.submit();
			v6.submit();
			v5.submit();
			v8.submit();
		}
        GL11.glEnd();

		// draw the bottom face
        Colour.WHITE.submit();
		GL11.glBegin(GL11.GL_POLYGON);
		{
			v3.submit();
			v4.submit();
			v1.submit();
			v2.submit();
		}
        GL11.glEnd();

		// draw the left face
        Colour.WHITE.submit();
		GL11.glBegin(GL11.GL_POLYGON);
		{
			v5.submit();
			v6.submit();
			v1.submit();
			v4.submit();
		}
        GL11.glEnd();

		// draw the right face
        Colour.WHITE.submit();
		GL11.glBegin(GL11.GL_POLYGON);
		{
			v7.submit();
			v8.submit();
			v3.submit();
			v2.submit();
		}
        GL11.glEnd();
	}
}
