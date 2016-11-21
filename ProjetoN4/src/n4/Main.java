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
import java.util.Random;

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
	private Random random = new Random();
	
	private int idTexture[];
	private int width, height;
	private BufferedImage image;
	private TextureData td;
	private ByteBuffer buffer[];
    
    private boolean eHMaterial = true;

	public void init(GLAutoDrawable drawable) {
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glut = new GLUT();
		glDrawable.setGL(new DebugGL(gl));

		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		//INICIA FUNDO
		idTexture = new int[3]; // lista de identificadores de textura
		gl.glGenTextures(1, idTexture, 2); 		// Gera identificador de textura
		// Define os filtros de magnificacao e minificacao 
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);	
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		buffer = new ByteBuffer [3]; // buffer da imagem
		loadImage(0,"space2.png"); // c
		loadImage(1,"space.jpg"); // c
		
		
		// INICIA AS CAMERAS
		initCameras();
		
		// INICIA NAVE
	    starSp = gl.glGenLists(1);
	    gl.glNewList(starSp, GL.GL_COMPILE);
		    initStarShip();
	    gl.glEndList();
		
		
		// INICIA ASTEROID
	    asteroids = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
		    initAsteroids();
		}
		for (Asteroid asteroid : asteroids) {
		    gl.glNewList(asteroid.getId(), GL.GL_COMPILE);
		    	asteroid.translacaoXYZ(random.nextInt(20), 0.0f , -20.0f);
		    	asteroid.drawAsteroid();
		    gl.glEndList();
		}
	    
		
		// INICIA BALAS
		bullets = new ArrayList<>();
		shots = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			initBullet();
		}
		for (Bullet bullet : bullets) {
	    	gl.glNewList(bullet.getId(), GL.GL_COMPILE);
	    		bullet.drawBullet();
	    	gl.glEndList();
	    }
		
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
	    glu.gluPerspective(60, width/height, 0.1, 800);				// projecao Perpectiva 1 pto fuga 3D    
//		gl.glFrustum (-5.0, 5.0, -5.0, 5.0, 10, 100);			// projecao Perpectiva 1 pto fuga 3D
//	    gl.glOrtho(-30.0f, 30.0f, -30.0f, 30.0f, -30.0f, 30.0f);	// projecao Ortogonal 3D

//		Debug();
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
		
		//DESENHA STARSHIP
		 gl.glPushMatrix();
			    gl.glPushMatrix();
			    gl.glTranslated(
			    		starShip.getMatrixObject().GetElement(12),
			    		starShip.getMatrixObject().GetElement(13),
			    		starShip.getMatrixObject().GetElement(14));
			    gl.glCallList(starSp); // Display List
			    gl.glPopMatrix();
			
			// DESENHA ASTEROIDS
			    for (Asteroid asteroid : asteroids) {
			    	gl.glPushMatrix();
					    gl.glTranslated( 0.0f , 0.0f , asteroid.getMoveAsteroid());
					    gl.glCallList(asteroid.getId()); // Display List
				    gl.glPopMatrix();
				}
			    
			// DESENHA BULLETS
			    for (Bullet bullet : shots) {
					gl.glPushMatrix();
						gl.glTranslated(
								bullet.getMatrixObject().GetElement(12),
								bullet.getMatrixObject().GetElement(13),
								bullet.getMatrixObject().GetElement(14) - bullet.getMoveBullet());
						
						gl.glCallList(bullet.getId()); // Display List
					gl.glPopMatrix();
			    }
		
			    
			 // DESENHA ESPAÇO TEXTURA
				gl.glPushMatrix();
					gl.glTranslatef(0.0f, 0.0f, 0.0f);
					gl.glEnable(GL.GL_TEXTURE_2D); // Primeiro habilita uso de textura
					gl.glBindTexture(GL.GL_TEXTURE_2D, idTexture[1]); 
					gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, width, height, 0, GL.GL_BGR, GL.GL_UNSIGNED_BYTE, buffer[1]);
						drawSpace();
					gl.glDisable(GL.GL_TEXTURE_2D); // Desabilita uso de textura
				gl.glPopMatrix();    
		    
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
			if (starShip != null && starShip.getMatrixObject().GetElement(12) < 19) {
					starShip.translacaoXYZ(1.0, 0.0, 0.0);
				System.out.println("	-- (L)to: 1.0, 0.0, 0.0 --	");
			}
			break;
		case KeyEvent.VK_LEFT: // MOVER ESQUERDA
			System.out.println("	-- VK_LEFT --	");
			if (starShip != null && starShip.getMatrixObject().GetElement(12) > -19) {
				starShip.translacaoXYZ(-1.0, 0.0, 0.0);
				System.out.println("	-- (L)to: -1.0, 0.0, 0.0 --	");
			}
			break;
		case KeyEvent.VK_UP: // MOVER CIMA
			System.out.println("	-- VK_UP --	");
			if (starShip != null && starShip.getMatrixObject().GetElement(14) > -40) {
				starShip.translacaoXYZ(0.0, 0.0, -1.0);
				System.out.println("	-- (L)to: 0.0, 0.0, -1.0 --	");
			}
			break;
		case KeyEvent.VK_DOWN: // MOVER BAIXO
			System.out.println("	-- VK_DOWN --	");
			if (starShip != null && starShip.getMatrixObject().GetElement(14) < 32) {
				starShip.translacaoXYZ(0.0, 0.0, 1.0);
				System.out.println("	-- (L)to: 0.0, 0.0, 1.0--	");
			}
			break;
			
		case KeyEvent.VK_ENTER: // MOVE ASTEROID ALEATORIOS
			System.out.println("	-- VK_ENTER --	");
			if (!asteroids.isEmpty()) {
			}
			break;
			
		case KeyEvent.VK_F: // MOVE ASTEROID ALEATORIOS
			System.out.println("	-- VK_F --	");
			
			if(!bullets.isEmpty()){
				
				Bullet b = bullets.get(0);
				bullets.remove(0);
				b.setMatrix(starShip.getMatrixObject());
				shots.add(b);
				
				
				}
			break;
		
		case KeyEvent.VK_1: // MOVE ASTEROID ALEATORIOS
			starShip.showMatrix();				
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
			Asteroid a = new Asteroid();
			a.atribuirGLs(gl, glut);
			a.setId(gl.glGenLists(1));
			asteroids.add(a);
		
	}

	private void initStarShip() {
			
		starShip = new StarShip();
		starShip.atribuirGLs(gl,glut);
		starShip.drawStarShip();
		starShip.translacaoXYZ(0.0f, 0.0f, 30.0f);
		
		
	}
	
	private void initBullet() {
		Bullet b = new Bullet();
		b.atribuirGLs(gl, glut);
		b.setId(gl.glGenLists(1));
		bullets.add(b);
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
