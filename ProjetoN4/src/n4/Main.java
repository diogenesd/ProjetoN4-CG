package n4;
/**
 * Objetivo: trabalha com conceitos da camera sintetica
 * https://www.opengl.org/sdk/docs/man2/xhtml/
 */

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.Random;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
//import javax.media.opengl.glu.GLUquadric;

import org.w3c.dom.events.EventTarget;
import org.w3c.dom.views.AbstractView;

import com.sun.opengl.util.GLUT;

public class Main implements GLEventListener, KeyListener, MouseListener, MouseMotionListener  {
	private GL gl;
	private GLU glu;
	private GLUT glut;
	private GLAutoDrawable glDrawable;
	private Camera cameraPrincipal, cameraAux, cameraTemp;
	private boolean camFocoPrincipal;
	private StarShip starShip;
	private Asteroid[] asteroids;
	private Bullet bullet;

    
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
		initStarShip();
		
		// INICIA ASTEROID
		initAsteroids();
		
		// INICIA BALAS
		initBullet();
		
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
        
        
        // DESENHA ESPAÇO
		drawAxis();
		
		//DESENHA STARSHIP
		starShip.drawStarShip();
		
		
		// DESENHA ASTEROIDS
		for (Asteroid asteroid : asteroids) {
			asteroid.drawAsteroid();
		}
				
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
		case KeyEvent.VK_M:
			eHMaterial = !eHMaterial;
			ligarLuz();
			break;
		
		// MOVE CAM UP
		case KeyEvent.VK_W:
			System.out.println("	-- VK_W --	");
			this.cameraPrincipal.translacaoXYZ(0.0f, 0.0f, 1.0f);
		break;
		// MOVE CAM UP
		case KeyEvent.VK_S:
			System.out.println("	-- VK_S --	");
			this.cameraPrincipal.translacaoXYZ(0.0f, 0.0f, -1.0f);
		break;
		// MOVE CAM RIGHT
		case KeyEvent.VK_D:
			System.out.println("	-- VK_D --	");
			this.cameraPrincipal.translacaoXYZ(1.0f, 0.0f, 0.0f);
		break;
		// MOVE CAM RIGHT
		case KeyEvent.VK_A:
			System.out.println("	-- VK_E --	");
			this.cameraPrincipal.translacaoXYZ(-1.0f, 0.0f, 0.0f);
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
			
		case KeyEvent.VK_ENTER: // MOVE ASTEROID ALEATORIOS
			System.out.println("	-- VK_ENTER --	");
			if (asteroids != null ) {
				aleatorieAsteroid();
			}
			break;
			
		}
		
		glDrawable.display();	
	}

	
	private void aleatorieAsteroid() {
			new Thread(new Runnable() {											
				@Override
				public void run() {
					while(true){
						try {
							for (Asteroid asteroid : asteroids) {
								System.out.println(asteroids.length);
								asteroid.translacaoXYZ(0.0f, 0.0f, 1.0f);
							}
							glDrawable.display();
							Thread.sleep(500);	
								
						} catch (InterruptedException e) {
							e.printStackTrace();
						}										
					}
				}
			}).start();
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
		this.cameraAux.setxEye(60.0f);
		this.cameraAux.setyEye(60.0f);
		this.cameraAux.setzEye(0.0f);
		this.cameraAux.setxCenter(0.0f);
		this.cameraAux.setyCenter(0.0f);
		this.cameraAux.setzCenter(0.0f);
		
		// CAMERA TEMP
		this.cameraTemp = new Camera();
	}
	
	private void initAsteroids() {
		asteroids = new Asteroid[3];
		asteroids[0] = new Asteroid();
		asteroids[1] = new Asteroid();
		asteroids[2] = new Asteroid();		
		
		asteroids[0].atribuirGLs(gl, glut);
		asteroids[1].atribuirGLs(gl, glut);
		asteroids[2].atribuirGLs(gl, glut);
		
		Random r = new Random();
		for (Asteroid asteroid : asteroids) {
			asteroid.randomPosition(
					 Double.parseDouble(String.valueOf(r.nextInt(20))),
					 0.0f,
					-50f);
		}
	}

	private void initStarShip() {
		starShip = new StarShip();
		starShip.atribuirGLs(gl,glut);
	}
	
	private void initBullet(){
		bullet = new Bullet();
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("    -- mouseClicked --  ");
		
		switch (e.getButton()) {
			case MouseEvent.BUTTON1: // DESENHAR VERTEX
				System.out.println("    -- CLICK MOUSE BUTTON LEFT --  ");
				this.bullet = new Bullet();
				this.bullet.drawBullet(
						starShip.getMatrixObject().GetDate()[0],
						starShip.getMatrixObject().GetDate()[5],
						starShip.getMatrixObject().GetDate()[10]);
				moveBullet();
			break;
		
		}
	}
	
	private void moveBullet() {
		new Thread(new Runnable() {											
			@Override
			public void run() {
				while(true){
					try {
						bullet.translacaoXYZ(0.0f, 0.0f, 1.0f);
						glDrawable.display();
						Thread.sleep(500);								
					} catch (InterruptedException e) {
						e.printStackTrace();
					}										
				}
			}
		}).start();
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("    -- mousePressed --  ");
	}



	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
}
