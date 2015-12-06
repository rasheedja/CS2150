/* CS2150Coursework.java
 * username: rasheeja
 * fullname: Junaid Ali Rasheed
 *
 * Scene Graph:
 *  Scene origin
 *  |
 *  +-- [S(25,1,25) T(0,-1,-12.5)] Ground plane
 *  |
 *  +-- [S(40,1,40) Rx(90) T(0,20,-20)] Sky plane
 *  |
 *  +-- [Rx(90) T(currentDeathStarX,currentDeathStarY,currentDeathStarZ)] Death Star
 *  |
 *  +-- [T(0,currentRocketY,-10)] Rocket
 *  |   |
 *  |   +-- [Rx(-90)] Cone
 *  |   |
 *  |   +-- [Rx(90)] Cylinder
 *  |   |
 *  |   +-- [S(1,1,currentFireSize) Rx(90) T(0,-2,0)]
 *  |
 *  +--[S(8,1.5,1) T(0,-0.5,-10)]
 */
package coursework.rasheeja;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;
import GraphicsLab.*;

/**
 * This submission shows a rocket flying upwards towards what looks like a moon but turns
 * out to be the Death Star from Star Wars. The animation ends on a cliff hanger.
 *
 * <p>Controls:
 * <ul>
 * <li>Press the escape key to exit the application.</li>
 * <li>Hold the x, y and z keys to view the scene along the x, y and z axis, respectively</li>
 * <li>While viewing the scene along the x, y or z axis, use the up and down cursor keys
 *      to increase or decrease the viewpoint's distance from the scene origin</li>
 * <li>Press E to start the animation.</li>
 * <li>press SPACE to reset the animation</li>
 * </ul>
 *
 * @author Junaid Ali Rasheed
 */
public class CS2150Coursework extends GraphicsLab
{
    /** display list id for the ground. */
    private final int groundList    = 1;
    /** display list id for the death star. */
    private final int deathStarList = 2;
    /** display list id for the sky plane. */
    private final int skyList       = 3;
    /** display list id for the platform. */
    private final int platList      = 4;

    /** the current position of the rocket. */
    private float currentRocketY = 2.0f;

    /** the current position of the death star. */
    private float currentDeathStarX = 5.0f;
    private float currentDeathStarY = 5.0f;
    private float currentDeathStarZ = -15.0f;

    /** whether the scene is being animated. */
    private boolean animating = false;

    /** the camera's current position. */
    private float currentCameraY = 0.0f;

    /** the current properties of the fire. */
    private float currentFireSize = 0.5f;
    private float currentFireRed = 0.25f;
    private float fireMinSize = 0.5f;
    private float fireMaxSize = 0.75f;
    private boolean fireRising = true;
    private boolean increaseFireRedness = true;

    /** id for the death star texture. */
    private Texture deathStarTexture;
    /** id for the ground plane texture. */
    private Texture grassTexture;
    /** id for the night sky texture. */
    private Texture nightSkyTexture;

    public static void main(String args[])
    {
        new CS2150Coursework().run(WINDOWED,"CS2150 Coursework Submission",0.01f);
    }

    /**
     * Set up the initial scene and display lists.
     * @throws Exception
     */
    protected void initScene() throws Exception
    {
        // load the textures
        deathStarTexture = loadTexture("coursework/rasheeja/textures/deathStar.jpg");
        grassTexture = loadTexture("coursework/rasheeja/textures/grass.jpg");
        nightSkyTexture = loadTexture("coursework/rasheeja/textures/nightSky.png");

        // create a very dim global ambient level
        float globalAmbient[]   = {0.1f,  0.1f,  0.1f, 1.0f};
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT,FloatBuffer.wrap(globalAmbient));

        // create a dim light above the initial viewpoint.
        float diffuse0[] = {1.0f, 1.0f, 1.0f, 1.0f};
        float ambient0[] = {0.1f, 0.1f, 0.1f, 1.0f};
        float position0[] = {0.0f, 10.0f, 0.0f, 1.0f};
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, FloatBuffer.wrap(ambient0));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, FloatBuffer.wrap(diffuse0));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, FloatBuffer.wrap(diffuse0));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, FloatBuffer.wrap(position0));
        GL11.glEnable(GL11.GL_LIGHT0);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_NORMALIZE);

        // set up the display lists
        GL11.glNewList(groundList,GL11.GL_COMPILE);
        {
            drawUnitPlane();
        }
        GL11.glEndList();
        GL11.glNewList(deathStarList,GL11.GL_COMPILE);
        {
            drawUnitPlane();
        }
        GL11.glEndList();
        GL11.glNewList(skyList,GL11.GL_COMPILE);
        {
            drawUnitPlane();
        }
        GL11.glEndList();
        GL11.glNewList(platList,GL11.GL_COMPILE);
        {
            drawUnitFrustum(Colour.WHITE, Colour.WHITE, Colour.WHITE, Colour.WHITE, Colour.WHITE, Colour.WHITE);
        }
        GL11.glEndList();

    }

    /**
     * Performs actions when certain keys are pressed.
     */
    protected void checkSceneInput()
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_E))
        {
            animating = true;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
        {
            resetAnimations();
        }
    }

    /**
     * Updates variables to make changes to the scene.
     */
    protected void updateScene()
    {
        // turn off the animation when the rocket has reached the final spot
        if(animating && currentRocketY > 6.0f)
        {
            animating = false;
        }

        // change the size of the fire
        if(animating && fireRising && currentFireSize <= fireMaxSize)
        {
            currentFireSize = currentFireSize + getAnimationScale();
        }
        if(animating && !fireRising && currentFireSize > fireMinSize)
        {
            currentFireSize = currentFireSize - getAnimationScale();
        }
        if(currentFireSize >= fireMaxSize)
        {
            fireRising = false;
        } else if (currentFireSize <= fireMinSize)
        {
            fireRising = true;
        }

        // move the rocket up and move the Death Star towards the rocket
        if(animating)
        {
            currentRocketY = currentRocketY + getAnimationScale();

            currentDeathStarX = currentDeathStarX - getAnimationScale();
            currentDeathStarY = currentDeathStarY + getAnimationScale()/2;
            currentDeathStarZ = currentDeathStarZ + getAnimationScale();
        }

        // change the colour/shininess of the fire
        if(animating && increaseFireRedness)
        {
            currentFireRed = currentFireRed + getAnimationScale()/10;
        }
        if(animating && !increaseFireRedness)
        {
            currentFireRed = currentFireRed - getAnimationScale()/10;
        }
        if(currentFireRed > 0.4f)
        {
            increaseFireRedness = false;
        }
        if(currentFireRed < 0.25)
        {
            increaseFireRedness = true;
        }

        // move the camera up with the rocket
        if(animating)
        {
            currentCameraY = currentCameraY + getAnimationScale();
        }
    }

    /**
     * Draws objects and loads textures for the scene.
     */
    protected void renderScene()
    {
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

        // draw the night sky
        GL11.glPushMatrix();
        {
            // set up the texture
            GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
            GL11.glDisable(GL11.GL_LIGHTING);
            Colour.WHITE.submit();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, nightSkyTexture.getTextureID());

            // position and create the sky
            GL11.glTranslatef(0.0f,20.0f,-20f);
            GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            GL11.glScalef(40.0f, 1.0f, 40.0f);
            GL11.glCallList(skyList);

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
            GL11.glTranslatef(currentDeathStarX, currentDeathStarY, currentDeathStarZ);
            GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            GL11.glCallList(deathStarList);

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopAttrib();
        }
        GL11.glPopMatrix();

        // draw the rocket
        GL11.glPushMatrix();
        {

            // position the rocket
            GL11.glTranslatef(0.0f, currentRocketY,-10f);

            // set the colour of the cone to blue
            float coneFrontSpecular[] = {0.0f, 0.0f, 0.2f, 1.0f};
            float coneFrontDiffuse[]  = {0.0f, 0.0f, 0.2f, 1.0f};
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(coneFrontSpecular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(coneFrontDiffuse));

            // position and create the cone
            GL11.glPushMatrix();
            {
                GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
                new Cylinder().draw(0.5f, 0.0f, 1.0f, 10, 10);
            }
            GL11.glPopMatrix();

            // set the colour of the cylindrical part of the rocket to red
            float rocketFrontSpecular[] = {0.2f, 0.0f, 0.0f, 1.0f};
            float rocketFrontDiffuse[]  = {0.2f, 0.0f, 0.0f, 1.0f};
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(rocketFrontSpecular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(rocketFrontDiffuse));

            // position and create the cylindrical part of the rocket
            GL11.glPushMatrix();
            {
                GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
                new Cylinder().draw(0.5f, 0.5f, 2.0f, 10, 10);
            }
            GL11.glPopMatrix();

            // set the colour of the fire to orange and make the fire glow when the rocket is flying
            float fireFrontEmissionOff[] = {0.0f, 0.0f, 0.0f, 1.0f};
            float fireFrontEmissionOn[]  = {currentFireRed, 0.13f, 0.0f, 1.0f};
            float fireFrontSpecular[]    = {currentFireRed, 0.13f, 0.0f, 1.0f};
            float fireFrontDiffuse[]     = {currentFireRed, 0.13f, 0.0f, 1.0f};

            if(currentRocketY > 2.0f)
            {
                GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION, FloatBuffer.wrap(fireFrontEmissionOn));
            } else {
                GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION, FloatBuffer.wrap(fireFrontEmissionOff));
            }
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(fireFrontSpecular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(fireFrontDiffuse));

            // position and create the fire
            GL11.glPushMatrix();
            {
                GL11.glTranslatef(0.0f, -2.0f, 0.0f);
                GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
                GL11.glScalef(1.0f, 1.0f, currentFireSize);
                new Cylinder().draw(0.25f, 0.0f, 1.0f, 10, 10);
            }
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();

        // draw the platform for the rocket
        GL11.glPushMatrix();
        {
            // set the colour to grey
            float fireFrontSpecular[] = {0.5f, 0.5f, 0.5f, 1.0f};
            float fireFrontDiffuse[]  = {0.5f, 0.5f, 0.5f, 1.0f};
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(fireFrontSpecular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(fireFrontDiffuse));

            // position and create the platform
            GL11.glScalef(8.0f,1.5f,1.0f);
            GL11.glTranslatef(0.0f,-0.5f,-10f);
            GL11.glCallList(platList);
        }
        GL11.glPopMatrix();
    }

    /**
     * Sets the initial camera for the scene and make sure that the camera is
     * following the rocket
     */
    protected void setSceneCamera()
    {
        super.setSceneCamera();

        // make the camera follow the rocket
        GLU.gluLookAt(0.0f, currentCameraY, 1.0f, 0.0f, currentCameraY, 0.0f, 0.0f, 1.0f, 0.0f);
   }

    protected void cleanupScene()
    {// empty
    }

    /**
     * Set all objects back to their defaults after they have been animated.
     */
    private void resetAnimations()
    {
        currentRocketY = 2.0f;
        currentFireSize = 0.5f;
        currentFireRed = 0.25f;

        currentDeathStarX = 5.0f;
        currentDeathStarY = 5.0f;
        currentDeathStarZ = -15.0f;

        currentCameraY = 0.0f;

        animating = false;
    }

    /**
     * Draws a unit plane.
     */
    public void drawUnitPlane()
    {
        Vertex v1 = new Vertex(-0.5f, 0.0f, 0.5f); // left,  front
        Vertex v2 = new Vertex( 0.5f, 0.0f, 0.5f); // right, front
        Vertex v3 = new Vertex( 0.5f, 0.0f,-0.5f); // right, back
        Vertex v4 = new Vertex(-0.5f, 0.0f,-0.5f); // left,  back

        // draw the plane
        GL11.glBegin(GL11.GL_POLYGON);
        {
            GL11.glTexCoord2f(0.0f,0.0f);
            v1.submit();

            GL11.glTexCoord2f(1.0f,0.0f);
            v2.submit();

            GL11.glTexCoord2f(1.0f,1.0f);
            v3.submit();

            GL11.glTexCoord2f(0.0f,1.0f);
            v4.submit();
        }
        GL11.glEnd();
    }

    /**
     * Draws a unit frustum using the given colours for its 6 faces.
     *
     * @param near      Colour for the frustum's near face
     * @param far       Colour for the frustum's far face
     * @param top       Colour for the frustum's top face
     * @param bottom    Colour for the frustum's bottom face
     * @param left      Colour for the frustum's left face
     * @param right     Colour for the frustum's right face
     */
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
        GL11.glEnd();

        // draw the top face
        top.submit();
        GL11.glBegin(GL11.GL_POLYGON);
        {
            v7.submit();
            v6.submit();
            v5.submit();
            v8.submit();
        }
        GL11.glEnd();

        // draw the bottom face
        bottom.submit();
        GL11.glBegin(GL11.GL_POLYGON);
        {
            v3.submit();
            v4.submit();
            v1.submit();
            v2.submit();
        }
        GL11.glEnd();

        // draw the left face
        left.submit();
        GL11.glBegin(GL11.GL_POLYGON);
        {
            v5.submit();
            v6.submit();
            v1.submit();
            v4.submit();
        }
        GL11.glEnd();

        // draw the right face
        right.submit();
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
