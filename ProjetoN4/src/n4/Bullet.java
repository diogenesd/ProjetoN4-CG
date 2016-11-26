package n4;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

public class Bullet extends ObjetoGrafico{

	private boolean eHMaterial = true;
	private GL gl;
	private GLUT glut;
	private float corYellow[] = { 1.0f, 1.0f, 0.0f, 1.0f };
	private float corRed[] = { 1.0f, 0.0f, 0.0f, 1.0f };
	private float[] cor;
	private BoundingBox bBox;
	public Transformacao4D matrixObject = new Transformacao4D();
	private double speed = 0.2f;
	private float size = 0.5f;
	private boolean ativo = true;
	private boolean explodiu = false;
	private int tempo;
	 private Timer timer;
	
	public Bullet(GL gl, GLUT glut) {
		super();
		bBox = new n4.BoundingBox();
		this.gl = gl;
		this.glut = glut;
	    tempo = 4;
	    cor = corYellow;
	    contador();
	}
	
	public void drawBbox() {
		gl.glColor3f(1.0f, 1.0f, 0f);	// COR AMARELA
		gl.glLineWidth(2.0f);
		gl.glPushMatrix();
			gl.glMultMatrixd(matrixObject.GetDate(), 0);
			//DESENHA A BBOX
			moveBullet();
			bBox.desenharOpenGLBBox(gl);
		gl.glPopMatrix();
		
	}
	public void atualizarBBox(float size) {
        n4.Ponto4D pontos[] = {new n4.Ponto4D(size * -1, size * -1, size * -1),
            new n4.Ponto4D(size, size, size),};

        // atualizar os pontos
        for (n4.Ponto4D ponto : pontos) {
            ponto.atribuirX(ponto.obterX() + matrixObject.GetElement(12));
            ponto.atribuirY(ponto.obterY() + matrixObject.GetElement(13));
            ponto.atribuirZ(ponto.obterZ() + matrixObject.GetElement(14));
        }

        // atualizar a Bbox
        bBox.setBoundingBox(pontos);
    }
	
	public void atualizarBBox() {
        float cordenada = size ; // 2.0f;
        atualizarBBox(cordenada);
	}
	public void showBbox() {
		System.out.println(this.bBox.toString());
	}
	public void atribuirIdentidade() {
		matrixObject.atribuirIdentidade();
	}
	
	public void drawBullet() {
		
		if (ativo && explodiu == false) {
			if (eHMaterial) {
				gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, cor, 0);
				gl.glEnable(GL.GL_LIGHTING);
			}
			
			gl.glPushMatrix();
			gl.glMultMatrixd(matrixObject.GetDate(), 0);
			gl.glTranslated(0.0f, 0.0f, 0.0f);
			gl.glScalef(size, size, size);
			glut.glutSolidCube(size);
			gl.glPopMatrix();
	
			if (eHMaterial) {
				gl.glDisable(GL.GL_LIGHTING);
			}
			
			atualizarBBox(size);
			moveBullet();
		}else if(ativo && explodiu == true){
			fire();
		}
		
	}
	public void fire() {
    	if (eHMaterial) {
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, corRed, 0);
			gl.glEnable(GL.GL_LIGHTING);
		}
        gl.glPushMatrix();
	        gl.glTranslated(matrixObject.GetElement(12), matrixObject.GetElement(13),matrixObject.GetElement(14));
	        gl.glScalef(6.0f, 6.0f, 6.0f);
	        glut.glutSolidIcosahedron();
        
        gl.glPopMatrix();
        if (eHMaterial) {
			gl.glDisable(GL.GL_LIGHTING);
		}
    }
	public void translacaoXYZ(double tx, double ty, double tz) {
		Transformacao4D mL = new Transformacao4D();
		mL.atribuirTranslacao(tx, ty, tz);
		matrixObject = mL.transformMatrix(matrixObject);
	}
	
	public void moveRigth(){
		Transformacao4D mL = new Transformacao4D();
		mL.atribuirTranslacao(speed, 0.0f, 0.0f);
		matrixObject = mL.transformMatrix(matrixObject);
	}
	public void moveLeft(){
		Transformacao4D mL = new Transformacao4D();
		mL.atribuirTranslacao(-speed, 0.0f, 0.0f);
		matrixObject = mL.transformMatrix(matrixObject);
	}
	public void moveUp(){
		Transformacao4D mL = new Transformacao4D();
		mL.atribuirTranslacao(0.0f, 0.0f, -speed);
		matrixObject = mL.transformMatrix(matrixObject);
	}
	public void moveDown(){
		Transformacao4D mL = new Transformacao4D();
		mL.atribuirTranslacao(0.0f, 0.0f, speed);
		matrixObject = mL.transformMatrix(matrixObject);
	}
	public void escalaXYZ(double Sx, double Sy) {
		Transformacao4D matrizScale = new Transformacao4D();
		matrizScale.atribuirEscala(Sx, Sy, 1.0);
		// ATRIBUI ALTERAÇÕES PARA A MATRIZ DESTE OBJETO
		matrixObject = matrizScale.transformMatrix(matrixObject);
	}
	
	public boolean colision(Asteroid asteroid) {

        if ((obterMaiorX() > asteroid.obterMenorX() && obterMenorX() < asteroid.obterMenorX()
                || (obterMaiorX() > asteroid.obterMaiorX() && obterMenorX() < asteroid.obterMaiorX())
                || (obterMaiorX() <= asteroid.obterMaiorX() && obterMenorX() >= asteroid.obterMenorX())
                || (obterMaiorX() == asteroid.obterMaiorX() && obterMenorX() == asteroid.obterMenorX()))){

            if ((obterMaiorZ() > asteroid.obterMenorZ() && obterMenorZ() < asteroid.obterMenorZ())
                    || (obterMaiorZ() > asteroid.obterMaiorZ() && obterMenorZ() < asteroid.obterMaiorZ())
                    || (obterMaiorZ() <= asteroid.obterMaiorZ()&& obterMenorZ() >= asteroid.obterMenorZ()
                    || (obterMaiorZ() == asteroid.obterMaiorZ() && obterMenorZ() == asteroid.obterMenorZ()))){

                if ((obterMaiorY() > asteroid.obterMenorY() && obterMenorY() < asteroid.obterMenorY())
                        || (obterMaiorY() > asteroid.obterMaiorY()&& obterMenorY() < asteroid.obterMaiorY())
                        || (obterMaiorY() <= asteroid.obterMaiorY()&& obterMenorY() >= asteroid.obterMenorY()
                        || (obterMaiorY() == asteroid.obterMaiorY() && obterMenorY() == asteroid.obterMenorY()))){
                	
                    return true;
                }
            }
        }
        return false;
    }
	
	public ArrayList<Asteroid> checkColisao(ArrayList<Asteroid> objetos) {
        ArrayList<Asteroid> colisionObj = new ArrayList<>();
        if (objetos != null) {
            for (Asteroid obj : objetos) {
                if (colision(obj)) {
                    colisionObj.add(obj);
                }

            }
        }
        return colisionObj;
    }
	
	 public final void contador() {
	        timer = new Timer();
	        timer.scheduleAtFixedRate(new TimerTask() {
	            @Override
	            public void run() {
	                setTempo();

	            }
	        }, 1000, 1000);
	    }

	    private int setTempo() {
	        switch (tempo) {
	            case 1:
	            	setAtivo(false);
	                timer.cancel();
	                break;
	        }
	        return --tempo;
	    }
	
	
    public boolean explodiu() {
        return tempo == 0;
    }

	public void showMatrix() {
		matrixObject.exibeMatriz();
	}
	
	public Transformacao4D getMatrixObject() {
		return this.matrixObject;
	}
	/**
	 * Retorna a maior coordenada X da BBox deste objeto
	 * @return double maior coordenada X 
	 */
	public double obterMaiorX() {
		return bBox.obterMaiorX();
	}

	/**
	 * Retorna a menor coordenada X da BBox deste objeto
	 * @return double menor coordenada X
	 */
	public double obterMenorX() {
		return bBox.obterMenorX();
	}

	/**
	 * Retorna a maior coordenada Y da BBox deste objeto
	 * @return double maior coordenada Y
	 */
	public double obterMaiorY() {
		return bBox.obterMaiorY();
	}

	/**
	 * Retorna a menor coordenada Y da BBox deste objeto
	 * @return double menor coordenada Y
	 */
	public double obterMenorY() {
		return bBox.obterMenorY();
	}
	
	public double obterMaiorZ() {
		return bBox.obterMaiorZ();
	}
	
	public double obterMenorZ() {
		return bBox.obterMenorZ();
	}

	/**
	 * Retorna a BBOX deste objeto
	 * @return BoundingBox do objeto
	 */
	public n4.BoundingBox getBbox() {
		return this.bBox;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	
	
	public boolean isExplodiu() {
		return explodiu;
	}

	public void setExplodiu(boolean explodiu) {
		this.explodiu = explodiu;
	}

	public void moveBullet() {
		moveUp();
	}
	
	@Override
	public void drawn() {
		if(ativo){
			drawBullet();
		}else{
			System.out.println(" -- EXPLODIU --");
		}
		
	}
	
	
}
