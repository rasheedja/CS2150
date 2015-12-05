/* CS2150Coursework.java
 * TODO: put your university username and full name here
 *
 * Scene Graph:
 *  Scene origin
 *  |
 *
 *  TODO: Provide a scene graph for your submission
 */
package coursework.rasheeja;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.newdawn.slick.opengl.Texture;
import GraphicsLab.*;

/**
 * TODO: Briefly describe your submission here
 *
 * <p>Controls:
 * <ul>
 * <li>Press the escape key to exit the application.
 * <li>Hold the x, y and z keys to view the scene along the x, y and z axis, respectively
 * <li>While viewing the scene along the x, y or z axis, use the up and down cursor keys
 *      to increase or decrease the viewpoint's distance from the scene origin
 * </ul>
 * TODO: Add any additional controls for your sample to the list above
 *
 */
public class CS2150Coursework extends GraphicsLab
{
    /** display list id for the ground */
    private final int groundList    = 1;
    /** display list id for the death star */
    private final int deathStarList = 2;
    /** display list id for the sky plane */
    private final int skyList       = 3;
    /** display list id for the platform */
    private final int platList      = 4;

    /** id for the death star texture */
    private Texture deathStarTexture;
    /** id for the ground plane texture */
    private Texture grassTexture;
    /** id for the night sky texture */
    private Texture nightSkyTexture;

    //TODO: Feel free to change the window title and default animation scale here
    public static void main(String args[])
    {   new CS2150Coursework().run(WINDOWED,"CS2150 Coursework Submission",0.01f);
    }

    protected void initScene() throws Exception
    {//TODO: Initialise your resources here - might well call other methods you write.

        // load the textures
        deathStarTexture = loadTexture("coursework/rasheeja/textures/deathStar.jpg");
        grassTexture = loadTexture("coursework/rasheeja/textures/grass.jpg");
        nightSkyTexture = loadTexture("coursework/rasheeja/textures/nightSky.png");

        // set the global ambient light level
        float globalAmbient[]   = {0.4f,  0.4f,  0.4f, 1.0f};
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT,FloatBuffer.wrap(globalAmbient));

        // the first light for the scene is white
        float diffuse0[] = { 1f,  1f, 1f, 1.0f};
        // and it is fairly dim
        float ambient0[] = { 0.1f, 0.1f, 0.1f, 1.0f};
        // and is positioned above the initial viewpoint
        float position0[] = { 0.0f, 10.0f, 0.0f, 1.0f};

        // supply OpenGL with the properties for the first light
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, FloatBuffer.wrap(ambient0));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, FloatBuffer.wrap(diffuse0));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, FloatBuffer.wrap(diffuse0));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, FloatBuffer.wrap(position0));
        // enable the first light
        GL11.glEnable(GL11.GL_LIGHT0);

        // enable lighting calculations
        GL11.glEnable(GL11.GL_LIGHTING);
        // ensure that all normals are re-normalised after transformations automatically
        GL11.glEnable(GL11.GL_NORMALIZE);

        GL11.glNewList(groundList,GL11.GL_COMPILE);
        {   drawUnitPlane();
        }
        GL11.glEndList();
        GL11.glNewList(deathStarList,GL11.GL_COMPILE);
        {   drawUnitPlane();
        }
        GL11.glEndList();
        GL11.glNewList(skyList,GL11.GL_COMPILE);
        {   drawUnitPlane();
        }
        GL11.glEndList();
        GL11.glNewList(platList,GL11.GL_COMPILE);
        {   drawUnitFrustum(Colour.WHITE, Colour.WHITE, Colour.WHITE, Colour.WHITE, Colour.WHITE, Colour.WHITE);
        }
        GL11.glEndList();

    }
    protected void checkSceneInput()
    {//TODO: Check for keyboard and mouse input here
    }
    protected void updateScene()
    {
        //TODO: Update your scene variables here - remember to use the current animation scale value
        //        (obtained via a call to getAnimationScale()) in your modifications so that your animations
        //        can be made faster or slower depending on the machine you are working on
    }
    protected void renderScene()
    {//TODO: Render your scene here - remember that a scene graph will help you write this method! 
     //      It will probably call a number of other methods you will write.
        // draw the ground plane
        GL11.glPushMatrix();
        {
            // set up the texture
            GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
            GL11.glDisable(GL11.GL_LIGHTING);
            Colour.WHITE.submit();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,grassTexture.getTextureID());

            // position and create the ground plane
            GL11.glTranslatef(0.0f,-1.0f,-12.5f);
            GL11.glScalef(25.0f, 1.0f, 25.0f);
            GL11.glCallList(groundList);

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopAttrib();
        }
        GL11.glPopMatrix();

        // draw the death star.
        GL11.glPushMatrix();
        {
            // set up the texture
            GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
            GL11.glDisable(GL11.GL_LIGHTING);
            Colour.WHITE.submit();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,deathStarTexture.getTextureID());

            // position and create the death star
            GL11.glTranslatef(5.0f, 5.0f, -15.0f);
            GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            GL11.glScalef(1, 1, 1);
            GL11.glCallList(deathStarList);

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopAttrib();
        }
        GL11.glPopMatrix();

        // draw the night sky
        GL11.glPushMatrix();
        {
            // set up the texture
            GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
            GL11.glDisable(GL11.GL_LIGHTING);
            Colour.WHITE.submit();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,nightSkyTexture.getTextureID());

            // position and create the sky
            GL11.glTranslatef(0.0f,20.0f,-20f);
            GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            GL11.glScalef(40.0f, 1.0f, 40.0f);
            GL11.glCallList(skyList);

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopAttrib();
        }
        GL11.glPopMatrix();

        // draw the main cylindrical part of the rocket
        GL11.glPushMatrix();
        {
            // set the colour to red
            float rocketFrontSpecular[] = {0.2f, 0.0f, 0.0f, 1.0f};
            float rocketFrontDiffuse[]  = {0.2f, 0.0f, 0.0f, 1.0f};
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(rocketFrontSpecular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(rocketFrontDiffuse));

            // position and create the cylindrical part of the rocket
            GL11.glTranslatef(0.0f,2.0f,-10f);
            GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            new Cylinder().draw(0.5f, 0.5f, 2.0f, 10, 10);

            GL11.glPopAttrib();
        }
        GL11.glPopMatrix();

        // draw the cone on the top of the rocket
        GL11.glPushMatrix();
        {
            // set the colour to blue
            float rocketFrontSpecular[] = {0.0f, 0.0f, 0.2f, 1.0f};
            float rocketFrontDiffuse[]  = {0.0f, 0.0f, 0.2f, 1.0f};
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(rocketFrontSpecular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(rocketFrontDiffuse));

            // position and create the cone
            GL11.glTranslatef(0.0f,2.0f,-10f);
            GL11.glRotatef(90.0f,-1.0f, 0.0f, 0.0f);
            new Cylinder().draw(0.5f, 0.0f, 1.0f, 10, 10);

            GL11.glPopAttrib();
        }
        GL11.glPopMatrix();

        // draw the fire at the bottom of the rocket
        GL11.glPushMatrix();
        {
            // set the colour to orange and make the fire glow
            float fireFrontEmission[]  = {0.5f, 0.26f, 0.0f, 1.0f};
            float fireFrontSpecular[] = {0.5f, 0.26f, 0.0f, 1.0f};
            float fireFrontDiffuse[]  = {0.5f, 0.26f, 0.0f, 1.0f};
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION, FloatBuffer.wrap(fireFrontEmission));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(fireFrontSpecular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(fireFrontDiffuse));

            // position and create the fire
            GL11.glTranslatef(0.0f,0.0f,-10f);
            GL11.glRotatef(90.0f,1.0f, 0.0f, 0.0f);
            new Cylinder().draw(0.25f, 0.0f, 1.0f, 10, 10);

            GL11.glPopAttrib();
        }
        GL11.glPopMatrix();

        // draw the night sky
        GL11.glPushMatrix();
        {
            Colour.WHITE.submit();
            // position and create the skyff
            GL11.glTranslatef(0.0f,0.0f,-5f);
            GL11.glCallList(platList);
            GL11.glPopAttrib();
        }
        GL11.glPopMatrix();



    }
    protected void setSceneCamera()
    {
        // call the default behaviour defined in GraphicsLab. This will set a default perspective projection
        // and default camera settings ready for some custom camera positioning below...  
        super.setSceneCamera();

        //TODO: If it is appropriate for your scene, modify the camera's position and orientation here
        //        using a call to GL11.gluLookAt(...)
   }

    protected void cleanupScene()
    {//TODO: Clean up your resources here
    }

    /**
     * Draws a plane which is aligned with the X and Y axis. The front face is
     * facing towards the positive Y axis.
     */
    public void drawUnitPlane()
    {
        Vertex v1 = new Vertex(-0.5f, 0.0f,-0.5f); // left,  back
        Vertex v2 = new Vertex( 0.5f, 0.0f,-0.5f); // right, back
        Vertex v3 = new Vertex( 0.5f, 0.0f, 0.5f); // right, front
        Vertex v4 = new Vertex(-0.5f, 0.0f, 0.5f); // left,  front

        // draw the ground plane with the vertices in the correct order
        GL11.glBegin(GL11.GL_POLYGON);
        {
            GL11.glTexCoord2f(0.0f,0.0f);
            v4.submit();

            GL11.glTexCoord2f(1.0f,0.0f);
            v3.submit();

            GL11.glTexCoord2f(1.0f,1.0f);
            v2.submit();

            GL11.glTexCoord2f(0.0f,1.0f);
            v1.submit();
        }
        GL11.glEnd();
    }

    public void drawUnitFrustum(Colour near, Colour far, Colour top, Colour bottom, Colour left, Colour right)
    {
        Vertex v1 = new Vertex(-0.5f,-0.5f,-0.5f); // left,  back,  bottom
        Vertex v2 = new Vertex( 0.5f,-0.5f,-0.5f); // right, back,  bottom
        Vertex v3 = new Vertex( 0.5f,-0.5f, 0.5f); // right, front, bottom
        Vertex v4 = new Vertex(-0.5f,-0.5f, 0.5f); // left,  front, bottom
        Vertex v5 = new Vertex(-0.3f, 0.5f, 0.5f); // left,  front, top
        Vertex v6 = new Vertex(-0.3f, 0.5f,-0.5f); // left,  back,  top
        Vertex v7 = new Vertex( 0.3f, 0.5f,-0.5f); // right, back,  top
        Vertex v8 = new Vertex( 0.3f, 0.5f, 0.5f); // right, front, top

        // draw the near face
        near.submit();
        GL11.glBegin(GL11.GL_POLYGON);
        {
            v5.submit();
            v4.submit();
            v3.submit();
            v8.submit();
        }
        GL11.glEnd();

        // draw the far face
        far.submit();
        GL11.glBegin(GL11.GL_POLYGON);
        {
            v6.submit();
            v7.submit();
            v2.submit();
            v1.submit();
        }

        // draw the top face
        top.submit();
        GL11.glBegin(GL11.GL_POLYGON);
        {
            v7.submit();
            v6.submit();
            v5.submit();
            v8.submit();
        }

        // draw the bottom face
        bottom.submit();
        GL11.glBegin(GL11.GL_POLYGON);
        {
            v3.submit();
            v4.submit();
            v1.submit();
            v2.submit();
        }

        // draw the left face
        left.submit();
        GL11.glBegin(GL11.GL_POLYGON);
        {
            v5.submit();
            v6.submit();
            v1.submit();
            v4.submit();
        }
        // draw the right face
        right.submit();
        GL11.glBegin(GL11.GL_POLYGON);
        {
            v7.submit();
            v8.submit();
            v3.submit();
            v2.submit();
        }
    }

}
