package vertices;

import processing.core.PApplet;
import processing.core.PVector;

public class Cube {

	PApplet p;
	
	PVector[] pos;
	
	PVector rad;
	PVector radI;
	PVector radO;
	
	float thetaX;
	float thetaX_coeff;

	float thetaY;
	float thetaY_coeff;
	
	float thetaZ;
	float thetaZ_coeff;

	PVector target;
	float targetX;
	float targetY;
	float targetZ;

	PVector val;
	PVector inc;
	float valX;
	float incX;
	float valY;
	float incY;
	float valZ;
	float incZ;
	
	float diagX;
	float diagY;
	float diagZ;
	
	float diagCoeffX;
	float diagCoeffY;
	float diagCoeffZ;

	float radIncI = 10;

	float radIncO = 50;

	int det = 1;
	
	int sw;
	int red;
	int green;
	int blue;
	
	PVector current;
	PVector start;
	PVector end;
	PVector trans;
	
	float move_val = 0;
	float move_inc = 0.1f;
	
	static boolean show = true;
	
	float diagSpeedX;
	float diagSpeedY;
	float diagSpeedZ;
	
	Cube(){}
	
	Cube(PApplet _p){
		this.p = _p;
		
		thetaX = 0;
		thetaX_coeff = 0.0001f;
		targetX = 0;
		valX = 0;
		incX = 0.01f;

		thetaY = 0;
		thetaY_coeff = 0.0001f;
		targetY = 0;
		valY = 0;
		incY = 0.01f;

		thetaZ = 0;
		thetaZ_coeff = 0.0001f;
		targetZ = 0;
		valZ = 0;
		incZ = 0.01f;
		
		rad = new PVector(200, 200, Vertices.c_depth);
		radI = new PVector(0, 0, 0);
		radO = new PVector (p.width, p.width, p.width);
		
		p.colorMode(PApplet.HSB, 360, 100, 100);
		red = p.color(0, 80, 50);
		green = p.color(120, 80, 50);
		blue = p.color(240, 80, 50);
		
		current = new PVector(0, 0, 0);
		start = new PVector(0, 0, 0);
		end = new PVector(0, 0, 0);
		trans = new PVector(0, 0, 100);
	}
	
	void update(){
		thetaX = PApplet.lerp(thetaX, targetX, valX);
		thetaY = PApplet.lerp(thetaY, targetY, valY);
		thetaZ = PApplet.lerp(thetaZ, targetZ, valZ);
		
		thetaX_coeff = Vertices.c_thetaX_coeff;
		thetaY_coeff = Vertices.c_thetaY_coeff;
		thetaZ_coeff = Vertices.c_thetaZ_coeff;

		if (valX < 1)
			valX += incX;

		if (valY < 1)
			valY += incY;

		if (valZ < 1)
			valZ += incZ;
		
		radIncI = PApplet.constrain(radIncI, 0.5f, 10);
		radIncO = PApplet.constrain(radIncO, 5, 100);

		if (radI.x < rad.x)
			radI.x += radIncI;
		if (radI.y < rad.y)
			radI.y += radIncI;
		else
			radI.y = rad.y;
		if (radI.z < rad.z)
			radI.z += radIncI;
		else
			radI.z = rad.z;
		
		
		
		if (radO.x > rad.x)
			radO.x -= radIncO;
		else
			radO.x = rad.x;
		if (radO.y > rad.y)
			radO.y -= radIncO;
		else
			radO.y = rad.y;
		if (radO.z > rad.z)
			radO.z -= radIncO;
		else
			radO.z = rad.z;
		
		rad.z = PApplet.constrain(Vertices.c_depth, 0, rad.x);
		rad.y = PApplet.constrain(Vertices.c_height, 0, rad.x);
		
		diagCoeffX = PApplet.constrain(diagCoeffX, 0, 10);
		diagCoeffY = PApplet.constrain(diagCoeffY, 0, 10);
		diagCoeffZ = PApplet.constrain(diagCoeffZ, 0, 10);
		
		diagSpeedX = PApplet.constrain(diagSpeedX, 0, 0.5f);
		diagSpeedY = PApplet.constrain(diagSpeedY, 0, 0.5f);
		diagSpeedZ = PApplet.constrain(diagSpeedZ, 0, 0.5f);
		
		diagX = PApplet.cos(p.millis()*Vertices.bpm*diagSpeedX)*diagCoeffX;
		diagY = PApplet.cos(p.millis()*Vertices.bpm*diagSpeedY)*diagCoeffY;
		diagZ = PApplet.cos(p.millis()*Vertices.bpm*diagSpeedZ)*diagCoeffZ;
		
		current = PVector.lerp(start, end, move_val);
		
		if(move_val < 1)
			move_val += move_inc;
		else{
			current = end.copy();
			start = end.copy();
		}
		
		trans.x = current.x + p.width*0.5f;
		trans.y = current.y + p.height*0.5f;
		
		sw = PApplet.constrain(sw, 1, 5);
	}
	
	void display(){
		p.noFill();
		p.pushMatrix();
		p.translate(trans.x, trans.y, trans.z);
		p.rotateX(thetaX+p.millis()*thetaX_coeff);
		p.rotateY(thetaY+p.millis()*thetaY_coeff);
		p.rotateZ(thetaZ+p.millis()*thetaZ_coeff);
		
		if(show)
			drawCube();
		
		p.popMatrix();
	}
	
	void drawCube() {
		p.strokeWeight(sw);
		
		p.stroke(red);//RED
		
		p.pushMatrix();
		p.rotateX(diagX);
		drawBox(rad);
		drawBox(radI);
		drawBox(radO);
		p.popMatrix();
		
		p.pushMatrix();
		p.rotateY(diagY);
		drawBox(rad);
		drawBox(radI);
		drawBox(radO);
		p.popMatrix();
		
		p.pushMatrix();
		p.rotateZ(diagZ);
		drawBox(rad);
		drawBox(radI);
		drawBox(radO);
		p.popMatrix();
		
		p.stroke(green); //GREEN
		p.pushMatrix();
		p.rotateX(diagX*2);
		drawBox(rad);
		drawBox(radI);
		drawBox(radO);
		p.popMatrix();
		
		p.pushMatrix();
		p.rotateY(diagY*2);
		drawBox(rad);
		drawBox(radI);
		drawBox(radO);
		p.popMatrix();
		
		p.pushMatrix();
		p.rotateZ(diagZ*2);
		drawBox(rad);
		drawBox(radI);
		drawBox(radO);
		p.popMatrix();
		
		
		p.stroke(blue);//BLUE
		
		p.pushMatrix();
		p.rotateX(-diagX);
		drawBox(rad);
		drawBox(radI);
		drawBox(radO);
		p.popMatrix();
		
		p.pushMatrix();
		p.rotateY(-diagY);
		drawBox(rad);
		drawBox(radI);
		drawBox(radO);
		p.popMatrix();
		
		p.pushMatrix();
		p.rotateZ(-diagZ);
		drawBox(rad);
		drawBox(radI);
		drawBox(radO);
		p.popMatrix();
		
		p.stroke(0, 0, 100, 100); //WHITE
		
		drawBox(rad);
		drawBox(radI);
		drawBox(radO);
	}
	
	void drawBox(PVector r){
		PVector[] pos = new PVector[8];
		
		pos[0] = new PVector(r.x*0.5f, r.y*0.5f, -r.z*0.5f);
		pos[1] = new PVector(-r.x*0.5f, r.y*0.5f, -r.z*0.5f);
		pos[2] = new PVector(-r.x*0.5f, r.y*0.5f, r.z*0.5f);
		pos[3] = new PVector(r.x*0.5f, r.y*0.5f, r.z*0.5f);
		pos[4] = new PVector(r.x*0.5f, -r.y*0.5f, r.z*0.5f);
		pos[5] = new PVector(-r.x*0.5f, -r.y*0.5f, r.z*0.5f);
		pos[6] = new PVector(-r.x*0.5f, -r.y*0.5f, -r.z*0.5f);
		pos[7] = new PVector(r.x*0.5f, -r.y*0.5f, -r.z*0.5f);
		
		for(int i = 0; i < pos.length-1; i++){
			p.line(pos[i].x, pos[i].y, pos[i].z, pos[i+1].x, pos[i+1].y, pos[i+1].z);
		}
		
		p.line(pos[0].x, pos[0].y, pos[0].z, pos[3].x, pos[3].y, pos[3].z);
		p.line(pos[0].x, pos[0].y, pos[0].z, pos[7].x, pos[7].y, pos[7].z);
		p.line(pos[5].x, pos[5].y, pos[5].z, pos[2].x, pos[2].y, pos[2].z);
		p.line(pos[6].x, pos[6].y, pos[6].z, pos[1].x, pos[1].y, pos[1].z);
		p.line(pos[7].x, pos[7].y, pos[7].z, pos[4].x, pos[4].y, pos[4].z);
	}
	
	public void keyPressed() {
		if (p.key == 'x') {
			targetX += PApplet.PI/2;
			valX = 0;
		}

		if (p.key == 'y') {
			targetY += PApplet.PI/2;
			valY = 0;
		}

		if (p.key == 'z') {
			targetZ += PApplet.PI/2;
			valZ = 0;
		}

		if (p.key == 'i')
			radI = new PVector(0, 0, 0);
	}
	
}
