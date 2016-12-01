package n4;
/**
 * Objetivo: trabalha com conceitos da camera sintetica
 * https://www.opengl.org/sdk/docs/man2/xhtml/
 */

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.j2d.TextRenderer;

public class Main implements GLEventListener, KeyListener{
	private GL gl;
	private GLU glu;
	private GLUT glut;
	private GLAutoDrawable glDrawable;
	private Camera cameraPrincipal, cameraAux, cameraTemp;
	private boolean camFocoPrincipal;
    
    private boolean eHMaterial = true;
    private boolean showBbox = false;
    
    private final Set<Integer> pressed = new HashSet<>();
	private World mundo;
	
	private boolean paused;
    private boolean start;
    
    private TextRenderer renderer;

	public void init(GLAutoDrawable drawable) {
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glut = new GLUT();
		glDrawable.setGL(new DebugGL(gl));

		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		mundo = new World(gl, glu, glut, drawable);
		
		// INICIA AS CAMERAS
		initCameras();
		
		// ILUMINACAO
		ligarLuz();
		
		paused = true;
        start = true;
		
		
	    gl.glEnable(GL.GL_CULL_FACE);
//	    gl.glDisable(GL.GL_CULL_FACE);	// DEFAULT OFF
		
	    gl.glEnable(GL.GL_DEPTH_TEST);  // 
//	    gl.glDisable(GL.GL_DEPTH_TEST);	// DEFAULT OFF DEVIDO AO CUSTO
	    
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	    gl.glMatrixMode(GL.GL_PROJECTION);
	    gl.glLoadIdentity();
		gl.glViewport(0, 0, width, height);
	    glu.gluPerspective(60, width/height, 0.1, 800);				// projecao Perpectiva 1 pto fuga 3D    
	}

	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
		//glu.gluLookAt(xEye, yEye, zEye, xCenter, yCenter, zCenter, 0.0f, 1.0f, 0.0f);
        
        if(camFocoPrincipal){
        	
        	this.cameraPrincipal.setxEye(mundo.getStarShip().getMatrixObject().GetElement(12));
	        //this.cameraPrincipal.setyEye();
	        this.cameraPrincipal.setzEye(mundo.getStarShip().getMatrixObject().GetElement(14)+10);
	        this.cameraPrincipal.setxCenter(mundo.getStarShip().getMatrixObject().GetElement(12));
	        this.cameraPrincipal.setyCenter(mundo.getStarShip().getMatrixObject().GetElement(13));
	        this.cameraPrincipal.setzCenter(0.0f);
        	
        glu.gluLookAt(this.cameraPrincipal.getxEye(), // Configuração da camera no viewPort
        		this.cameraPrincipal.getyEye(),
        		this.cameraPrincipal.getzEye(),
        		this.cameraPrincipal.getxCenter(), 
        		this.cameraPrincipal.getyCenter(),
        		this.cameraPrincipal.getzCenter(),
        		0.0f, 1.0f, 0.0f);
        }else{
		        this.cameraAux.setxEye(mundo.getStarShip().getMatrixObject().GetElement(12));
		        this.cameraAux.setyEye(mundo.getStarShip().getMatrixObject().GetElement(13));
		        this.cameraAux.setzEye(mundo.getStarShip().getMatrixObject().GetElement(14));
		        this.cameraAux.setxCenter(mundo.getStarShip().getMatrixObject().GetElement(12));
		        this.cameraAux.setyCenter(mundo.getStarShip().getMatrixObject().GetElement(13));
		        this.cameraAux.setzCenter(mundo.getStarShip().getMatrixObject().GetElement(14) - 60);
        	
		        glu.gluLookAt(this.cameraAux.getxEye(), // Configuração da camera no viewPort
		        		this.cameraAux.getyEye(),
		        		this.cameraAux.getzEye(),
		        		this.cameraAux.getxCenter(), 
		        		this.cameraAux.getyCenter(),
		        		this.cameraAux.getzCenter(),
		        		0.0f, 1.0f, 0.0f);
        }
        
    	gl.glPushMatrix();
    	
		        if (paused) {
		            pause(start);
		        } else if (mundo.getStarShip().isAtivo()) {
		            
		        	// CAMERA GIRAR
		            //this.cameraTemp.drawn();
		            
		            // DESENHA ESPAÇO
		    		//drawAxis();
		    		
		    		// DESENHA MUNDO
		    		mundo.desenha();
		    		
		    		// ATUALIZA OS EVENTOS 		
		    		updateEvents();
		        	
		        	
		        } else {
		            fim();
		        }
        
		gl.glPopMatrix();	
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

	public synchronized void keyPressed(KeyEvent e) {
		
		if (!paused) {
			pressed.add(e.getKeyCode());
			
			switch (e.getKeyCode()) {
			
			// CHANGE CAMERA
			case KeyEvent.VK_C:
				changeCam();
			break;
			
			// CHANGE COLOR MATERIAL
			case KeyEvent.VK_M:
				eHMaterial = !eHMaterial;
				ligarLuz();
			break;
			
			// SHOW MATRIX STARTSHIP
			case KeyEvent.VK_1:
				mundo.getStarShip().showMatrix();
			break;
			
			// SHOW BBOX
			case KeyEvent.VK_B:
				showBbox = !showBbox;
				System.out.println("ASTEROID");
				mundo.getAsteroids().get(0).showBbox();
				System.out.println("STARSHIP");
				mundo.getStarShip().showBbox();
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
			
			// MOVE CAM RIGHT
			case KeyEvent.VK_L:
				System.out.println("	-- VK_L --	");
				mundo.getStarShip().loop(glDrawable);
				break;
				
			// MOVE CAM RIGHT
			case KeyEvent.VK_R:
				System.out.println("	-- VK_R --	");
				new animacao("").start();
				break;
				
			case KeyEvent.VK_P:
				System.out.println("	-- VK_P --	");
				paused = true;
				break;	
				
			// BULLET
			case KeyEvent.VK_F:
				System.out.println("	-- VK_F --	");
				if (mundo != null && mundo.getStarShip() != null) {
					mundo.addBUllets(mundo.getStarShip());
				}
				break;	
			}

			glDrawable.display();
			
        } else {
        	
            unpause();
        }

	}
	
	@Override
    public synchronized void keyReleased(KeyEvent e) {
        pressed.remove(e.getKeyCode());
    }

	public void updateEvents() {
		
		// TRANSLAÇÕES (L)
		if (pressed.contains(KeyEvent.VK_RIGHT)) {
			System.out.println("	-- VK_RIGHT --	");
			if (mundo.getStarShip() != null && mundo.getStarShip().getMatrixObject().GetElement(12) < 19) {

				mundo.getStarShip().moveRigth();
				ArrayList<Asteroid> objs = mundo.getStarShip().checkColisao(mundo.getAsteroids());
				if (objs.isEmpty()) {
					 System.out.println("	-- (L)to: 1.0, 0.0, 0.0 --	");
				 }else{
					 System.out.println("	-- ******************** Colisão ******************** --	");
					 mundo.getStarShip().setAtivo(false);
				 }
				
			}
		}
		if (pressed.contains(KeyEvent.VK_LEFT)) {
			System.out.println("	-- VK_LEFT --	");
			if (mundo.getStarShip() != null && mundo.getStarShip().getMatrixObject().GetElement(12) > -19) {
				
				mundo.getStarShip().moveLeft();
				 ArrayList<Asteroid> objs = mundo.getStarShip().checkColisao(mundo.getAsteroids());
				 if (objs.isEmpty()) {
					 System.out.println("	-- (L)to: -1.0, 0.0, 0.0 --	");
				 }else{
					 System.out.println("	-- ******************** Colisão ******************** --	");
					 mundo.getStarShip().setAtivo(false);
				 }
			}
		}
		if (pressed.contains(KeyEvent.VK_UP)) {
			System.out.println("	-- VK_UP --	");
			if (mundo.getStarShip() != null && mundo.getStarShip().getMatrixObject().GetElement(14) > -40) {
				
				mundo.getStarShip().moveUp();
				 ArrayList<Asteroid> objs = mundo.getStarShip().checkColisao(mundo.getAsteroids());
				 if (objs.isEmpty()) {
					 System.out.println("	-- (L)to: 0.0, 0.0, -1.0 --	");
				 }else{
					 System.out.println("	-- ******************** Colisão ******************** --	");
					 mundo.getStarShip().setAtivo(false);
				 }
			}
		}
		if (pressed.contains(KeyEvent.VK_DOWN)) {
			System.out.println("	-- VK_DOWN --	");
			if (mundo.getStarShip() != null && mundo.getStarShip().getMatrixObject().GetElement(14) < 32) {
				
				mundo.getStarShip().moveDown();
				 ArrayList<Asteroid> objs = mundo.getStarShip().checkColisao(mundo.getAsteroids());
				 if (objs.isEmpty()) {
					 System.out.println("	-- (L)to: 0.0, 0.0, 1.0 --	");
				 }else{
					 System.out.println("	-- ******************** Colisão ******************** --	");
					 mundo.getStarShip().setAtivo(false);
				 }
			}
		}
		
		//ASTEROID
		if (pressed.contains(KeyEvent.VK_NUMPAD2)) {
			System.out.println("	-- VK_NUMPAD2 --	");
			if (!mundo.getAsteroids().isEmpty() ) {
				mundo.getAsteroids().get(0).moveDown();
				System.out.println("	-- (L)to: 0.0, 0.0, 1.0--	");
			}
		}
		if (pressed.contains(KeyEvent.VK_NUMPAD8)) {
			System.out.println("	-- VK_NUMPAD8 --	");
			if (!mundo.getAsteroids().isEmpty() ) {
				mundo.getAsteroids().get(0).moveUp();
				System.out.println("	-- (L)to: 0.0, 0.0, -1.0--	");
			}
		}
		if (pressed.contains(KeyEvent.VK_NUMPAD6)) {
			System.out.println("	-- VK_NUMPAD6 --	");
			if (!mundo.getAsteroids().isEmpty()) {
				mundo.getAsteroids().get(0).moveRigth();
				System.out.println("	-- (L)to: 1.0, 0.0, 0.0--	");
			}
		}
		if (pressed.contains(KeyEvent.VK_NUMPAD4)) {
			System.out.println("	-- VK_NUMPAD4 --	");
			if (!mundo.getAsteroids().isEmpty() ) {
				mundo.getAsteroids().get(0).moveLeft();
				System.out.println("	-- (L)to: -1.0, 0.0, 0.0--	");
			}
		}
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

	public void keyTyped(KeyEvent arg0) {

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
			System.out.println(cameraPrincipal.toString());
		}
	}
	
	
	private void initCameras(){
		
		// CAMERA CENTRAL
		this.cameraPrincipal = new Camera(gl, glut);
		this.cameraPrincipal.setxEye(0.0f);
		this.cameraPrincipal.setyEye(10.0f);
		this.cameraPrincipal.setzEye(60.0f);
		this.cameraPrincipal.setxCenter(0.0f);
		this.cameraPrincipal.setyCenter(0.0f);
		this.cameraPrincipal.setzCenter(0.0f);
		camFocoPrincipal = true;
		
		// CAMERA LATERAL
		this.cameraAux = new Camera(gl, glut);
		this.cameraAux.setxEye(0.0f);
		this.cameraAux.setyEye(0.0f);
		this.cameraAux.setzEye(0.0f);
		this.cameraAux.setxCenter(0.0f);
		this.cameraAux.setyCenter(0.0f);
		this.cameraAux.setzCenter(0.0f);
		
		// CAMERA TEMP
		this.cameraTemp = new Camera(gl, glut);
	}

	public float RetornaX(double angulo, double raio) {
		return (float) (raio * Math.cos(Math.PI * angulo / 180.0));
	}
	
	public float RetornaY(double angulo, double raio) {
		return (float) (raio * Math.sin(Math.PI * angulo / 180.0));
	}
	
	class animacao extends Thread {
	    public animacao(String str) {
	    	super(str);
	    }
	    public void run() {
			for (int i = 0; i < 360; i++) {
				cameraTemp.Animacao();
				glDrawable.display();  // redesenhar .
			}
		}
	}
	
	 private void pause(boolean start) {

	        Cor corFundo = Cor.BLACK;
	        gl.glClearColor(corFundo.getR(), corFundo.getG(), corFundo.getB(), corFundo.getA());

	        String text = "Press any key to ";
	        int width = glDrawable.getWidth();
	        int height = glDrawable.getHeight();
	        int x;
	        int y = height / 6;
	        if (start) {
	            text += "start";
	            x = width / 6;
	        } else {
	            text += "continue";
	            x = width / 11;
	        }

	        renderer = new TextRenderer(new Font("SansSerif", Font.BOLD, width / 15), true, true);
	        renderer.beginRendering(width, height);
	        renderer.setSmoothing(true);
	        Cor cor = Cor.WHITE;
	        renderer.setColor(cor.getR(), cor.getG(), cor.getB(), cor.getA());
	        renderer.draw(text, x, y);
	        renderer.endRendering();
	        
	    }

	    private void unpause() {
	        paused = false;
	        start = false;
	    }
	    
	    private void fim() {

	        Cor corFundo = Cor.BLACK;
	        gl.glClearColor(corFundo.getR(), corFundo.getG(), corFundo.getB(), corFundo.getA());

	        String text = " Game Over!";
	        int width = glDrawable.getWidth();
	        int height = glDrawable.getHeight();
	        int x;
	        int y = height / 2;
	        x = width / 2;

	        renderer = new TextRenderer(new Font("SansSerif", Font.BOLD, width / 15), true, true);
	        renderer.beginRendering(width, height);
	        renderer.setSmoothing(true);
	        Cor cor = Cor.WHITE;
	        renderer.setColor(cor.getR(), cor.getG(), cor.getB(), cor.getA());
	        renderer.draw(text, x, y);
	        renderer.endRendering();
	        
	    }
	
}
