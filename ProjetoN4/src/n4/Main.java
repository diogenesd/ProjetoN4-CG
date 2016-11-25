package n4;
/**
 * Objetivo: trabalha com conceitos da camera sintetica
 * https://www.opengl.org/sdk/docs/man2/xhtml/
 */

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
//import javax.media.opengl.glu.GLUquadric;
import javax.swing.JOptionPane;

import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.texture.TextureData;

public class Main implements GLEventListener, KeyListener, MouseListener{
	private GL gl;
	private GLU glu;
	private GLUT glut;
	private GLAutoDrawable glDrawable;
	private Camera cameraPrincipal, cameraAux, cameraTemp;
	private boolean camFocoPrincipal;
	private StarShip starShip;
	private ArrayList<Asteroid> asteroids;
	private ArrayList<Bullet> bullets;
	private ArrayList<Bullet> shots;
	
	private int starSp;
	
	private int idTexture[];
	private int width, height;
	private BufferedImage image;
	private TextureData td;
	private ByteBuffer buffer[];
    
    private boolean eHMaterial = true;
    private boolean showBbox = false;
    
    private final Set<Integer> pressed = new HashSet<>();
	private World mundo;

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
		
		
		/*// INICIA BALAS
		bullets = new ArrayList<>();
		shots = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			initBullet();
		}
		for (Bullet bullet : bullets) {
	    	gl.glNewList(bullet.getId(), GL.GL_COMPILE);
	    		bullet.drawBullet();
	    	gl.glEndList();
	    }*/
		
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
	    glu.gluPerspective(60, width/height, 0.1, 800);				// projecao Perpectiva 1 pto fuga 3D    
	}

	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
		//glu.gluLookAt(xEye, yEye, zEye, xCenter, yCenter, zCenter, 0.0f, 1.0f, 0.0f);
        
        if(camFocoPrincipal){
        glu.gluLookAt(this.cameraPrincipal.getxEye(), // Configuração da camera no viewPort
        		this.cameraPrincipal.getyEye(),
        		this.cameraPrincipal.getzEye(),
        		this.cameraPrincipal.getxCenter(), 
        		this.cameraPrincipal.getyCenter(),
        		this.cameraPrincipal.getzCenter(),
        		0.0f, 1.0f, 0.0f);
        }else{
		        this.cameraAux.setxEye(starShip.getMatrixObject().GetElement(12));
		        this.cameraAux.setyEye(starShip.getMatrixObject().GetElement(13));
		        this.cameraAux.setzEye(starShip.getMatrixObject().GetElement(14));
		        this.cameraAux.setxCenter(starShip.getMatrixObject().GetElement(12));
		        this.cameraAux.setyCenter(starShip.getMatrixObject().GetElement(13));
		        this.cameraAux.setzCenter(starShip.getMatrixObject().GetElement(14) - 60);
		        glu.gluLookAt(this.cameraAux.getxEye(), // Configuração da camera no viewPort
		        		this.cameraAux.getyEye(),
		        		this.cameraAux.getzEye(),
		        		this.cameraAux.getxCenter(), 
		        		this.cameraAux.getyCenter(),
		        		this.cameraAux.getzCenter(),
		        		0.0f, 1.0f, 0.0f);
        }
        // DESENHA ESPAÇO
		drawAxis();
		
		// DESENHA MUNDO
		mundo.desenha();
		
		// ATUALIZA OS EVENTOS 		
		updateEvents();
		
		gl.glPushMatrix();
		
					// DESENHA ASTEROIDS
					/*for (Asteroid asteroid : asteroids) {
						gl.glPushMatrix();
							gl.glTranslated(
									asteroid.getMatrixObject().GetElement(12),
									asteroid.getMatrixObject().GetElement(13),
									asteroid.getMatrixObject().GetElement(14)); //+ asteroid.getMoveAsteroid());
									asteroid.atualizarBBox();
							gl.glCallList(asteroid.getId()); // Display List
						gl.glPopMatrix();
						if (showBbox) {
							asteroid.drawBbox();
						}
					}*/
						    
						    
					/*// DESENHA BULLETS
					    for (Bullet bullet : shots) {
							gl.glPushMatrix();
								gl.glTranslated(
										bullet.getMatrixObject().GetElement(12),
										bullet.getMatrixObject().GetElement(13),
										bullet.getMatrixObject().GetElement(14) - bullet.getMoveBullet());
								
								gl.glCallList(bullet.getId()); // Display List
							gl.glPopMatrix();
					    }*/
					    
		    
		gl.glPopMatrix();	
		gl.glFlush();
	}
	
	public void drawSpace() {
		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 0.0f, 0.0f);
		gl.glScalef(200.0f, 200.0f, 200.0f);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glBegin (GL.GL_QUADS );
			// Especifica a coordenada de textura para cada vertice
			
		// Face Posterior
		gl.glNormal3f(0.0f,0.0f,-1.0f);
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f, -1.0f);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f,  1.0f, -1.0f);	
		
		// Face Frontal
		gl.glNormal3f(0.0f,0.0f,1.0f);
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, 1.0f);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f,  1.0f, 1.0f);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f, 1.0f);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, 1.0f);
		
		// Face Inferior
		gl.glNormal3f(0.0f,1.0f,0.0f);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f,  1.0f);
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f,  1.0f);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);
		
		// Face Superior
		gl.glNormal3f(0.0f,-1.0f,0.0f);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, 1.0f, -1.0f);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f, 1.0f, -1.0f);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 1.0f, 1.0f,  1.0f);
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, 1.0f,  1.0f);
		
		// Face Esquerda
		gl.glNormal3f(1.0f,0.0f,0.0f);
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( -1.0f, -1.0f, -1.0f);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( -1.0f,  1.0f, -1.0f);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( -1.0f,  1.0f,  1.0f);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( -1.0f, -1.0f,  1.0f);
					
		// Face Direita
		gl.glNormal3f(-1.0f,0.0f,0.0f);
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f,  1.0f);
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f,  1.0f);
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f, -1.0f);
		
		
		gl.glEnd();
	gl.glPopMatrix();
		
	}

	public void loadImage(int ind, String fileName)
	{
		// Tenta carregar o arquivo		
		image = null;
		try {
			image = ImageIO.read(new File(fileName));
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null,"Erro na leitura do arquivo "+fileName);
		}

		// Obtem largura e altura
		width  = image.getWidth();
		height = image.getHeight();
		// Gera uma nova TextureData...
		td = new TextureData(0,0,false,image);
		// ...e obtem um ByteBuffer a partir dela
		buffer[ind] = (ByteBuffer) td.getBuffer();
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
			starShip.showMatrix();
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
		}

		glDrawable.display();
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
		
		// BULLET
		if (pressed.contains(KeyEvent.VK_F)) {
			System.out.println("	-- VK_F --	");
			if (mundo != null && mundo.getStarShip() != null) {
				mundo.addBUllets(mundo.getStarShip());
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
			System.out.println(cameraPrincipal.toString());
		}
	}
	
	
	private void initCameras(){
		
		// CAMERA CENTRAL
		this.cameraPrincipal = new Camera();
		this.cameraPrincipal.setxEye(0.0f);
		this.cameraPrincipal.setyEye(10.0f);
		this.cameraPrincipal.setzEye(100.0f);
		this.cameraPrincipal.setxCenter(0.0f);
		this.cameraPrincipal.setyCenter(0.0f);
		this.cameraPrincipal.setzCenter(0.0f);
		camFocoPrincipal = true;
		
		// CAMERA LATERAL
		this.cameraAux = new Camera();
		this.cameraAux.setxEye(0.0f);
		this.cameraAux.setyEye(0.0f);
		this.cameraAux.setzEye(0.0f);
		this.cameraAux.setxCenter(0.0f);
		this.cameraAux.setyCenter(0.0f);
		this.cameraAux.setzCenter(0.0f);
		
		// CAMERA TEMP
		this.cameraTemp = new Camera();
	}
	
	private void initAsteroids() {
		
		/*for (int i = 0; i < 2 ; i++) {
			Asteroid a = new Asteroid();
			a.atribuirGLs(gl, glut);
			a.setId(gl.glGenLists(1));
			asteroids.add(a);
		}*/
			
	}

	private void initStarShip() {
			
		/*starShip = new StarShip();
		starShip.atribuirGLs(gl,glut);
		starShip.drawStarShip();*/
		
	}
	
	private void initBullet() {
		/*Bullet b = new Bullet();
		b.atribuirGLs(gl, glut);
		b.setId(gl.glGenLists(1));
		bullets.add(b);*/
	}
	
	private void posiciona(){
		//starShip.translacaoXYZ(0.0f, 0.0f, 30.f);
		
		/*int limite = 10;
		for (ObjetoGrafico asteroid : mundo.getAsteroids()) {
			asteroid.translacaoXYZ(limite, 0.0f, 30.f);
			limite *= -1;
		}*/
	}



	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
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


	public float RetornaX(double angulo, double raio) {
		return (float) (raio * Math.cos(Math.PI * angulo / 180.0));
	}
	
	public float RetornaY(double angulo, double raio) {
		return (float) (raio * Math.sin(Math.PI * angulo / 180.0));
	}
	
	
	
}
