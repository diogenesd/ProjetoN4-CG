package n4;
/**
 * Objetivo: trabalha com conceitos da camera sintetica
 * https://www.opengl.org/sdk/docs/man2/xhtml/
 */

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
//import javax.media.opengl.glu.GLUquadric;

import com.sun.opengl.util.GLUT;

public class Main implements GLEventListener, KeyListener {
	private GL gl;
	private GLU glu;
	private GLUT glut;
	private GLAutoDrawable glDrawable;
	private Camera cameraPrincipal, cameraAux, cameraTemp;
	private boolean camFocoPrincipal;
	private StarShip starShip;
	private Asteroid[] asteroids;

	private float translacaoCubo1[] = { 0.0f, 0.0f, 0.0f };
	private float escalaCubo1[]     = { 2.0f, 2.0f, 2.0f };
	private float translacaoCubo2[] = { 0.0f, 0.0f, 1.5f };
	private float escalaCubo2[]     = { 1.0f, 1.0f, 1.0f };

    private float corRed[] = { 1.0f, 0.0f, 0.0f, 1.0f };
//  private float corGreen[] = { 0.0f, 1.0f, 0.0f, 1.0f };
//  private float corBlue[] = { 0.0f, 0.0f, 1.0f, 1.0f };
//  private float corWhite[] = { 1.0f, 1.0f, 1.0f, 1.0f };
//  private float corBlack[] = { 0.0f, 0.0f, 0.0f, 1.0f };
    
    private boolean eHMaterial = true;

	public void init(GLAutoDrawable drawable) {
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glut = new GLUT();
		glDrawable.setGL(new DebugGL(gl));

		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		// INICIA AS CAMERAS
		initCameras();
		
		// INICIA NAVE
		starShip = new StarShip();
		starShip.atribuirGLs(gl,glut);
		
		// INICIA ASTEROID
		asteroids = new Asteroid[3];
		asteroids[0] = new Asteroid();
		asteroids[1] = new Asteroid();
		asteroids[2] = new Asteroid();		
		
		asteroids[0].atribuirGLs(gl, glut);
		asteroids[1].atribuirGLs(gl, glut);
		asteroids[2].atribuirGLs(gl, glut);
		
		asteroids[0].translacaoXYZ(4.0f, 0.0f, -10.0f);
		asteroids[1].translacaoXYZ(-4.0f,0.0f, -10.0f);
		asteroids[2].translacaoXYZ(-8.0f,0.0f, -10.0f);
		
		// ILUMINACAO
		ligarLuz();
		
	    gl.glEnable(GL.GL_CULL_FACE);
//	    gl.glDisable(GL.GL_CULL_FACE);	// DEFAULT OFF
		
	    gl.glEnable(GL.GL_DEPTH_TEST);  // 
//	    gl.glDisable(GL.GL_DEPTH_TEST);	// DEFAULT OFF DEVIDO AO CUSTO
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	    gl.glMatrixMode(GL.GL_PROJECTION);
	    gl.glLoadIdentity();
		gl.glViewport(0, 0, width, height);

//		glu.gluOrtho2D(-30.0f, 30.0f, -30.0f, 30.0f);
	    glu.gluPerspective(60, width/height, 0.1, 100);				// projecao Perpectiva 1 pto fuga 3D    
//		gl.glFrustum (-5.0, 5.0, -5.0, 5.0, 10, 100);			// projecao Perpectiva 1 pto fuga 3D
//	    gl.glOrtho(-30.0f, 30.0f, -30.0f, 30.0f, -30.0f, 30.0f);	// projecao Ortogonal 3D

//		Debug();
	}

	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
		//glu.gluLookAt(xEye, yEye, zEye, xCenter, yCenter, zCenter, 0.0f, 1.0f, 0.0f);
        glu.gluLookAt(this.cameraPrincipal.getxEye(), // Configuração da camera no viewPort
        		this.cameraPrincipal.getyEye(),
        		this.cameraPrincipal.getzEye(),
        		this.cameraPrincipal.getxCenter(), 
        		this.cameraPrincipal.getyCenter(),
        		this.cameraPrincipal.getzCenter(),
        		0.0f, 1.0f, 0.0f);
        
        
		drawAxis();
		//gl.glColor3f(1.0f, 0.0f, 0.0f);
		starShip.drawCube();
		asteroids[0].drawAsteroid();
		asteroids[1].drawAsteroid();
		asteroids[2].drawAsteroid();
		
		
				
		gl.glFlush();
	}
	
	private void ligarLuz() {
		if (eHMaterial) {
			float posLight[] = { 5.0f, 5.0f, 10.0f, 0.0f };
			gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, posLight, 0);
			gl.glEnable(GL.GL_LIGHT0);
		}
		else
			gl.glDisable(GL.GL_LIGHT0);
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {

		case KeyEvent.VK_SPACE:
			changeCam();
			break;
		case KeyEvent.VK_1:
			this.cameraPrincipal.setxEye(5.0f);	//xEye = 5.0f;
			this.cameraPrincipal.setyEye(5.0f);	//yEye = 5.0f;
			this.cameraPrincipal.setzEye(5.0f);	//zEye = 5.0f;
			System.out.println(cameraPrincipal.toString());
			break;
		case KeyEvent.VK_M:
			eHMaterial = !eHMaterial;
			ligarLuz();
			break;
			
		// TRANSLAÇÕES (L)
		case KeyEvent.VK_RIGHT: // MOVER DIREITA
			System.out.println("	-- VK_RIGHT --	");
			if (starShip != null) {
					starShip.translacaoXYZ(1.0, 0.0, 0.0);
				System.out.println("	-- (L)to: 3.0, 0.0, 0.0 --	");
			}
			break;
		case KeyEvent.VK_LEFT: // MOVER ESQUERDA
			System.out.println("	-- VK_LEFT --	");
			if (starShip != null) {
				starShip.translacaoXYZ(-1.0, 0.0, 0.0);
				System.out.println("	-- (L)to: -2.0, 0.0, 0.0 --	");
			}
			break;
		case KeyEvent.VK_UP: // MOVER CIMA
			System.out.println("	-- VK_UP --	");
			if (starShip != null) {
				starShip.translacaoXYZ(0.0, 0.0, -1.0);
				System.out.println("	-- (L)to: 0.0, 2.0, 0.0 --	");
			}
			break;
		case KeyEvent.VK_DOWN: // MOVER BAIXO
			System.out.println("	-- VK_DOWN --	");
			if (starShip != null ) {
				starShip.translacaoXYZ(0.0, 0.0, 1.0);
				System.out.println("	-- (L)to: 0.0, -2.0, 0.0 --	");
			}
			break;
		case KeyEvent.VK_F1: // MOVE ASTEROID LEFT
			System.out.println("	-- VK_F1 --	");
			if (asteroids != null ) {
				aleatorieAsteroid();
			}
			break;
			
		}
		
		glDrawable.display();	
	}

	public void drawAxis() {
		// eixo X - Red
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3f(0.0f, 0.0f, 0.0f);
			gl.glVertex3f(10.0f, 0.0f, 0.0f);
		gl.glEnd();
		// eixo Y - Green
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3f(0.0f, 0.0f, 0.0f);
			gl.glVertex3f(0.0f, 10.0f, 0.0f);
		gl.glEnd();
		// eixo Z - Blue
		gl.glColor3f(0.0f, 0.0f, 1.0f);
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3f(0.0f, 0.0f, 0.0f);
			gl.glVertex3f(0.0f, 0.0f, 10.0f);
		gl.glEnd();
	}

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	private void changeCam(){
		if(camFocoPrincipal){
			this.cameraTemp = this.cameraPrincipal;
			this.cameraPrincipal = cameraAux;
			camFocoPrincipal = !camFocoPrincipal;
			System.out.println(cameraPrincipal.toString());
			
		}else{
			this.cameraPrincipal = cameraTemp;
			camFocoPrincipal = !camFocoPrincipal;
		}
	}
	
	private void initCameras(){
		
		// CAMERA CENTRAL
		this.cameraPrincipal = new Camera();
		this.cameraPrincipal.setxEye(0.0f);
		this.cameraPrincipal.setyEye(20.0f);
		this.cameraPrincipal.setzEye(20.0f);
		this.cameraPrincipal.setxCenter(0.0f);
		this.cameraPrincipal.setyCenter(0.0f);
		this.cameraPrincipal.setzCenter(0.0f);
		camFocoPrincipal = true;
		
		// CAMERA LATERAL
		this.cameraAux = new Camera();
		this.cameraAux.setxEye(20.0f);
		this.cameraAux.setyEye(20.0f);
		this.cameraAux.setzEye(0.0f);
		this.cameraAux.setxCenter(0.0f);
		this.cameraAux.setyCenter(0.0f);
		this.cameraAux.setzCenter(0.0f);
		
		// CAMERA TEMP
		this.cameraTemp = new Camera();
	}

	private void aleatorieAsteroid() {
		new Thread(new Runnable() {											
			@Override
			public void run() {
				while(true){
					try {
						asteroids[0].translacaoXYZ(0.0f, 0.0f, 1.0f);
						asteroids[1].translacaoXYZ(0.0f, 0.0f, 1.0f);
						asteroids[2].translacaoXYZ(0.0f, 0.0f, 1.0f);
						glDrawable.display();
						Thread.sleep(1000);									
					} catch (InterruptedException e) {
						e.printStackTrace();
					}										
				}
			}
		}).start();
	}
	
}
