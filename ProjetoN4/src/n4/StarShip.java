package n4;

import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import com.sun.opengl.util.GLUT;

import object.OBJModel;

public class StarShip extends ObjetoGrafico{
	 
	private boolean eHMaterial = true;
	private GL gl;
	private GLUT glut;
	private float corRed[] = { 1.0f, 0.0f, 0.0f, 1.0f };
	private BoundingBox bBox;
	public Transformacao4D matrixObject = new Transformacao4D();
	private double speed = 0.3f;
	private float size = 3.0f;
	private boolean ativo;
	private ArrayList<Bullet> bullets;
	
	   //armazena o modelo utilizado
    protected OBJModel[] loader = new OBJModel[7];
    //informa qual objeto esta sendo mostrado na tela
    protected int indexOBJ = 0;
	
	public StarShip(GL gl, GLUT glut) {
		super();
		bBox = new BoundingBox();
		this.gl = gl;
		this.glut = glut;
		
		bullets = new ArrayList<Bullet>();
		this.ativo = true;
        loader[0] = new OBJModel("data/XWing", 1.5f, gl, true);
        // nome correto p/ referencia qnd descobrir o problema do mtl: XWingLow-MediumPolyByPixelOzMultiMaterialColoredVer1-0

		
	}
	
	public void drawBbox() {
		gl.glColor3f(1.0f, 1.0f, 0f);	// COR AMARELA
		gl.glLineWidth(2.0f);
		gl.glPushMatrix();
			gl.glMultMatrixd(matrixObject.GetDate(), 0);
			//DESENHA A BBOX
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
	
	public void drawStarShip() {
		
		if (ativo) {
			if (eHMaterial) {
				gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, corRed, 0);
				gl.glEnable(GL.GL_LIGHTING);
			}
			gl.glPushMatrix();
			gl.glMultMatrixd(matrixObject.GetDate(), 0);
			gl.glTranslated(0.0f, 0.0f, 0.0f);
			gl.glScalef(size, size, size);
			//glut.glutSolidCube(2.0f);
            loader[indexOBJ].draw(gl);
			gl.glPopMatrix();

			if (eHMaterial) {
				gl.glDisable(GL.GL_LIGHTING);
			}
			//float size = 1;
			atualizarBBox(size);
		}else{
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
    
    
	public Bullet shootBullet(ArrayList<Asteroid> objetos) {
		
		Bullet bullet = new Bullet(gl, glut);
		
		bullet.translacaoXYZ(this.matrixObject.GetElement(12),
				this.matrixObject.GetElement(13),
				this.matrixObject.GetElement(14));

		bullet.atualizarBBox();

		ArrayList<Asteroid> objetosColidem = bullet.checkColisao(objetos);
		
		if (objetosColidem.isEmpty()) {
			bullets.add(bullet);
			return bullet;
		}else{
			for (Asteroid asteroid : objetosColidem) {
				System.out.println(" -- Colisão --");
				asteroid.showBbox();
			}
		}

		return null;
	}
    
	public void removeBullet(Bullet bullet) {
		bullets.remove(bullet);
	}

	public ArrayList<Bullet> getBullets() {
		return bullets;
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
	public void loop(GLAutoDrawable glDrawable) {
		new animacao(glDrawable).start();
	}
	public void giraX(double angulo) {
		Transformacao4D matrizRotacaoY = new Transformacao4D();		
		matrizRotacaoY.atribuirRotacaoX(Transformacao4D.DEG_TO_RAD * angulo);
		matrixObject = matrizRotacaoY.transformMatrix(matrixObject);
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
	
	public void setBullets(ArrayList<Bullet> bullets) {
		this.bullets = bullets;
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

	@Override
	public void drawn() {
		drawStarShip();
		
	}
	
	class animacao extends Thread {
		
	    private GLAutoDrawable drawable;
		public animacao(GLAutoDrawable glDrawable) {
	    	super();
	    	drawable = glDrawable;
	    }
	    public void run() {
	    	Transformacao4D mL = new Transformacao4D();
	    	mL.SetData(getMatrixObject().GetDate());
	    	for (int i = 0; i < 360; i++) {
	    		giraX(1);
	    		drawable.display();
			}
	    	corrigi(mL);
	    }
		private void corrigi(Transformacao4D mL) {
			matrixObject.SetData(mL.GetDate());;
		}
	}
	
}
