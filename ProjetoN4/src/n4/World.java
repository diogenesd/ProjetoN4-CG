package n4;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import com.sun.opengl.util.GLUT;

public class World {
	
	private GL gl;
	private GLU glu;
	private GLUT glut;
	private GLAutoDrawable glDrawable;
	
	private Set<ObjetoGrafico> objetos;
	private Set<ObjetoGrafico> objetosInativos;
	private ArrayList<Asteroid> asteroids;
	private Espaco espaco;
	private StarShip starShip;
	
	private int tempo;
	private Timer timer;
	
	public World(GL gl, GLU glu, GLUT glut, GLAutoDrawable drawable) {
		glDrawable = drawable;
		gl = gl;
		glu = glu;
		glut = glut;
		glDrawable.setGL(new DebugGL(gl));
		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		objetos = new HashSet<>();
		objetosInativos = new HashSet<>();
		asteroids = new ArrayList<>();
		
		espaco = new Espaco(gl);	
		objetos.add(espaco);
		
		starShip = new StarShip(gl, glut);
		objetos.add(starShip);
		
		contador(gl, glut);
		
	}
	
	public void desenha() {
		
		if(!asteroids.isEmpty()){
			objetos.addAll(asteroids);
		}
		
		if (!objetosInativos.isEmpty()) {
			for (ObjetoGrafico objeto : objetosInativos) {
				objetos.remove(objeto);
			}
		}
		// limpar lista inativa
		objetosInativos.clear();
		
		for (ObjetoGrafico objeto : objetos) {

			System.out.println("tam: " + objetos.size());

			if (objeto.isAtivo()) {

				if (objeto instanceof Bullet) {
					Bullet b = (Bullet) objeto;
					ArrayList<Asteroid> objc = b.checkColisao(asteroids);
					if (!objc.isEmpty()) {
						System.out.println(" COLISAO BULLET");
						b.setExplodiu(true);
						objetosInativos.add(b);
						for (ObjetoGrafico ob : objc) {
							objetosInativos.add(ob);
							asteroids.remove(ob);
						}
					}
				}

				objeto.drawn();

			} else {
				objetosInativos.add(objeto);
			}
		}
	}
	
	public void addBUllets(StarShip starShip) {
        Bullet bullet = starShip.shootBullet(asteroids);
        if (bullet != null) {
            objetos.add(bullet);
        }
    }
	
	public Set<ObjetoGrafico> getObjetos() {
		return objetos;
	}

	public void setObjetos(Set<ObjetoGrafico> objetos) {
		this.objetos = objetos;
	}
	
	public ArrayList<Asteroid> getAsteroids() {
		return asteroids;
	}

	public void setAsteroids(ArrayList<Asteroid> asteroids) {
		this.asteroids = asteroids;
	}

	public Espaco getEspaco() {
		return espaco;
	}

	public void setEspaco(Espaco espaco) {
		this.espaco = espaco;
	}

	public StarShip getStarShip() {
		return starShip;
	}

	public void setStarShip(StarShip starShip) {
		this.starShip = starShip;
	}
	
	public final void contador(GL gl, GLUT glut) {
        timer = new Timer();
        Random r = new Random();
        
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
            	int pos = -30;
            	Asteroid a = new Asteroid(gl, glut);
    			asteroids.add(a);
    			a.translacaoXYZ(pos += r.nextInt(40), 0.0f, -60.f);
    			
            }
        }, 400, 1000);
    }
	
}
