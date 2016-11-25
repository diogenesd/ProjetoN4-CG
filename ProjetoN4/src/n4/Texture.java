package n4;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.swing.JOptionPane;

import com.sun.opengl.util.texture.TextureData;

public final class Texture {

	private int[] idTexture;
	private int width, height;
	private BufferedImage image;
	private TextureData td;
	private ByteBuffer buffer[];

	public Texture(GL gl) {

		// INICIA FUNDO
		idTexture = new int[3]; 			// lista de identificadores de textura
		gl.glGenTextures(1, idTexture, 2); 	// Gera identificador de textura

		// Define os filtros de magnificacao e minificacao
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);

		buffer = new ByteBuffer[3]; 		// buffer da imagem
		loadImage(0, "estrelas2.png"); 
	}

	private void loadImage(int ind, String fileName) {
		
		image = null;
		try {
			image = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erro na leitura do arquivo " + fileName);
		}

		
		width = image.getWidth();		// Obtem largura 
		height = image.getHeight();		// Obtem altura
		
		
		td = new TextureData(0, 0, false, image);	// Gera uma nova TextureData...
		
		buffer[ind] = (ByteBuffer) td.getBuffer();	// ...e obtem um ByteBuffer a partir dela
	}

	

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public ByteBuffer[] getBuffer() {
		return buffer;
	}

	public void setBuffer(ByteBuffer[] buffer) {
		this.buffer = buffer;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int[] getIdTexture() {
		return idTexture;
	}

	public void setIdTexture(int[] idTexture) {
		this.idTexture = idTexture;
	}

	public TextureData getTd() {
		return td;
	}

	public void setTd(TextureData td) {
		this.td = td;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	

}
