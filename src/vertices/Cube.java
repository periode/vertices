package vertices;

import processing.core.PApplet;
import processing.core.PVector;

public class Cube {

	PApplet p;
	
	PVector[] pos;
	PVector[] pos_abs;
	PVector[] pulse;
	PVector[] pulse_origin;
	PVector[] pulse_target;
	float[] pulse_val;
	boolean[] canPulse;
	float pulse_inc;
	
	float maxRad;
	PVector rad;
	PVector radI;
	PVector radO;
	
	float thetaX;
	float thetaX_coeff;

	float thetaY;
	float thetaY_coeff;
	
	float thetaZ;
	float thetaZ_coeff;
	
	float theta_inc = 0.000005f;
	float max_theta = 0.000075f;

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
	float innerTheta = 0;
	
	boolean canRotateStep = true;

	float radIncO = 20;

	int det = 1;
	
	int sw;
	int red;
	int green;
	int blue;
	float cube_alpha = 0;
	float diagonals_alpha = 0;
	float cube_alpha_rate = 0.5f;
	
	PVector current;
	PVector start;
	PVector end;
	PVector trans;
	
	float move_val = 0;
	float move_inc = 0.1f;
	
	static boolean show = true;
	PVector canExpand = new PVector(1, 1, 1);
	boolean canScale;
	float expand_rate = 3f;
	
	static boolean isDrumming = false;
	
	boolean constant_rotateX = false;
	boolean constant_rotateY = false;
	boolean constant_rotateZ = false;
	
	float diagSpeedX;
	float diagSpeedY;
	float diagSpeedZ;
	
	float cube_scale = 1f;
	
	float max_depth = 0;
	float max_width = 0;
	
	float circles_alpha = 0;
	float circles_alpha_rate = 1f;
	
	boolean canShowCircles = false;
	boolean canShowDiagonals = false;
	boolean canShowEdges = true;
	
	boolean canFizzleSphere = false;
	boolean canFizzleCube = true;
	boolean canFizzleMoreCube = false;
	
	float fizzleRate = 0f;
	
	int distortVertices = 0;
	
	Cube(){}
	
	Cube(PApplet _p){
		this.p = _p;
		
		thetaX = 0;
		thetaX_coeff = 0f;
		targetX = 0;
		valX = 0;
		incX = 0.01f;

		thetaY = 0;
		thetaY_coeff = 0f;
		targetY = 0;
		valY = 0;
		incY = 0.01f;

		thetaZ = 0;
		thetaZ_coeff = 0f;
		targetZ = 0;
		valZ = 0;
		incZ = 0.01f;
		
		maxRad = p.width*0.15f;
		rad = new PVector(0, 0, 0);
		radI = new PVector(0, 0, 0);
		radO = new PVector (0, 0, 0);//fyi this used to be "width, width, width"
		
		current = new PVector(0, 0, 0);
		start = new PVector(0, 0, 0);
		end = new PVector(0, 0, 0);
		trans = new PVector(0, 0, 200);
		
		pulse = new PVector[8];
		pulse_origin = new PVector[8];
		pulse_target = new PVector[8];
		pulse_val = new float[8];
		canPulse = new boolean[8];
		pulse_inc = 0.1f;
		
		pos = new PVector[8];
		pos_abs = new PVector[8];
		
		for(int i = 0; i < pulse.length; i++){
			pulse[i] = new PVector(0, 0, 0);
			pulse_origin[i] = new PVector(0, 0, 0);
			
			
			pos[i] = new PVector(0, 0, 0);
			pos_abs[i] = new PVector(0, 0, 0);
		}
		

//		drawBox(rad);
	}
	
	void update(){
		//------ ROTATION
		thetaX = PApplet.lerp(thetaX, targetX, valX);
		thetaY = PApplet.lerp(thetaY, targetY, valY);
		thetaZ = PApplet.lerp(thetaZ, targetZ, valZ);
		
//		thetaX_coeff = Vertices.c_thetaX_coeff;
//		thetaY_coeff = Vertices.c_thetaY_coeff;
//		thetaZ_coeff = Vertices.c_thetaZ_coeff;

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
		
		if(rad.z < max_depth)
			rad.z += 10f;
		
		if(rad.y < max_width)
			rad.y += 10f;
		
//		rad.z = PApplet.constrain(Vertices.c_depth, 0, rad.x);
//		rad.y = PApplet.constrain(Vertices.c_height, 0, rad.x);
		
		if(canExpand.x == 1 && rad.x < maxRad)
			rad.x += expand_rate;
		
		if(canExpand.y == 1 && rad.y < maxRad)
			rad.y += expand_rate;
		
		if(canExpand.z == 1 && rad.z < maxRad)
			rad.z += expand_rate;
			
		rad.z = PApplet.constrain(rad.z, 0, rad.x);
		rad.y = PApplet.constrain(rad.y, 0, rad.x);
		
		
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
		
		for(int i =0; i < pulse.length; i++){
			if(canPulse[i]){
				pulse[i] = PVector.lerp(pulse_origin[i], pulse_target[i], pulse_val[i]);
				
				if(pulse_val[i] < 1)
					pulse_val[i] += pulse_inc;
				else{
					pulse_origin[i] = pulse[i].copy();
					canPulse[i] = false;
				}
			}
		}
		
		trans.x = current.x + p.width*0.5f;
		trans.y = current.y + p.height*0.5f;
		
		sw = PApplet.constrain(sw, 1, 5);
		
		if(canScale && this.cube_scale < 1)
			cube_scale += expand_rate*0.01f;
		
		//fizzle
		if(canFizzleSphere){
			if(fizzleRate < 0.25f)
				fizzleRate += 0.1f;
		}else if(!canShowCircles && !canFizzleCube){
			if(fizzleRate > 0)
				fizzleRate -= 0.1f;
		}
		
		if(canFizzleMoreCube){
			if(fizzleRate < 0.75f)
				fizzleRate += 0.1f;
		}else if(canFizzleCube){
			if(fizzleRate < 0.5f)
				fizzleRate += 0.1f;
		}
		
		
		
		//hide cube
		if(!canShowEdges){
			if(cube_alpha > 0){
				cube_alpha -= cube_alpha_rate;
			}
		}else{
			if(cube_alpha < 255){
				cube_alpha += cube_alpha_rate;
			}
		}
		
		//show/hide sphere
		if(canShowCircles){
			if(circles_alpha < 255)
				circles_alpha+= circles_alpha_rate;
		}else{
			if(circles_alpha > 0)
				circles_alpha-=circles_alpha_rate*5;
		}
		
		
		//show/hide diagonals
		if(canShowDiagonals){
			if(diagonals_alpha < 255){
				diagonals_alpha += cube_alpha_rate;
			}
		}else{
			if(diagonals_alpha > 0)
				diagonals_alpha -= cube_alpha_rate;
		}
		
	}
	

	
	public void rotateCube(String axis, int dir){
		if(axis == "X"){
			targetX += PApplet.PI/2*dir;
			valX = 0;
		}else if(axis == "Y"){
			targetY += PApplet.PI/2*dir;
			valY = 0;
		}else if(axis == "Z"){
			targetZ += PApplet.PI/2*dir;
			valZ = 0;
		}else{
			PApplet.println("wrong axis, bae.");
		}
	}
	
	public void resetCube(){
		for(int i = 0; i < pulse_target.length; i++){
			pulse_target[i] = new PVector(0, 0, 0);
			canPulse[i] = true;
			pulse_val[i] = 0;
		}
	}
	
	void display(){
		p.noFill();
		p.fill(0);
		p.pushMatrix();
		p.translate(trans.x, trans.y, trans.z);
		p.rotateX(thetaX+p.millis()*thetaX_coeff);
		p.rotateY(thetaY+p.millis()*thetaY_coeff);
		p.rotateZ(thetaZ+p.millis()*thetaZ_coeff);
		p.scale(cube_scale);
		
//		if(show)
			drawCube();
		
		p.popMatrix();
	}
	
	void drawCube() {
//		p.strokeWeight(sw);
		p.strokeWeight(1);
//		p.fill(0);
		p.stroke(100, 100, 255, cube_alpha);//RED
		
		p.pushMatrix();
		p.translate(p.random(-1f, 1f)*fizzleRate, p.random(-1f, 1f)*fizzleRate, p.random(-1f, 1f)*fizzleRate);
		p.rotateX(diagX);
		drawBox(rad, 0);
		drawBox(radI, 0);
		drawBox(radO, 0);
		p.popMatrix();
		
		p.pushMatrix();
		p.translate(p.random(-1f, 1f)*fizzleRate, p.random(-1f, 1f)*fizzleRate, p.random(-1f, 1f)*fizzleRate);
		p.rotateY(diagY);
		drawBox(rad, 0);
		drawBox(radI, 0);
		drawBox(radO, 0);
		p.popMatrix();
		
		p.pushMatrix();
		p.translate(p.random(-1f, 1f)*fizzleRate, p.random(-1f, 1f)*fizzleRate, p.random(-1f, 1f)*fizzleRate);
		p.rotateZ(diagZ);
		drawBox(rad, 0);
		drawBox(radI, 0);
		drawBox(radO, 0);
		p.popMatrix();
		
		
		p.stroke(255, cube_alpha); //GREEN
		
		p.pushMatrix();
		p.translate(p.random(-1f, 1f)*fizzleRate, p.random(-1f, 1f)*fizzleRate, p.random(-1f, 1f)*fizzleRate);
		p.rotateX(diagX*2);
		drawBox(rad, 0);
		drawBox(radI, 0);
		drawBox(radO, 0);
		p.popMatrix();
		
		p.pushMatrix();
		p.translate(p.random(-1f, 1f)*fizzleRate, p.random(-1f, 1f)*fizzleRate, p.random(-1f, 1f)*fizzleRate);
		p.rotateY(diagY*2);
		drawBox(rad, 0);
		drawBox(radI, 0);
		drawBox(radO, 0);
		p.popMatrix();
		
		p.pushMatrix();
		p.translate(p.random(-1f, 1f)*fizzleRate, p.random(-1f, 1f)*fizzleRate, p.random(-1f, 1f)*fizzleRate);
		p.rotateZ(diagZ*2);
		drawBox(rad, 0);
		drawBox(radI, 0);
		drawBox(radO, 0);
		p.popMatrix();
		
		
		p.stroke(255, cube_alpha);//BLUE
		
		p.pushMatrix();
		p.translate(p.random(-1f, 1f)*fizzleRate, p.random(-1f, 1f)*fizzleRate, p.random(-1f, 1f)*fizzleRate);
		p.rotateX(-diagX);
		drawBox(rad, 0);
		drawBox(radI, 0);
		drawBox(radO, 0);
		p.popMatrix();
		
		p.pushMatrix();
		p.rotateY(-diagY);
		drawBox(rad, 0);
		drawBox(radI, 0);
		drawBox(radO, 0);
		p.popMatrix();
		
		p.pushMatrix();
		p.translate(p.random(-1f, 1f)*fizzleRate, p.random(-1f, 1f)*fizzleRate, p.random(-1f, 1f)*fizzleRate);
		p.rotateZ(-diagZ);
		drawBox(rad, 1);
		drawBox(radI, 0);
		drawBox(radO, 0);
		p.popMatrix();
		
		p.stroke(255, cube_alpha*0.5f); //WHITE - ALPHA
		p.strokeWeight(2);
		drawBox(rad, 1);
		
		p.strokeWeight(1);
		drawBox(radI, 0);
		drawBox(radO, 0);
		
		p.stroke(255); //WHITE - FULL
		
		if(canShowCircles)
			drawCircles(rad);
	}
	
	void drawBox(PVector r, int type){
		PVector[] pos = new PVector[8];
		
		pos[0] = new PVector(r.x*0.5f+pulse[0].x, r.y*0.5f+pulse[0].y, -r.z*0.5f+pulse[0].z);
		pos[1] = new PVector(-r.x*0.5f+pulse[1].x, r.y*0.5f+pulse[1].y, -r.z*0.5f+pulse[1].z);
		pos[2] = new PVector(-r.x*0.5f+pulse[2].x, r.y*0.5f+pulse[2].y, r.z*0.5f+pulse[2].z);
		pos[3] = new PVector(r.x*0.5f+pulse[3].x, r.y*0.5f+pulse[3].y, r.z*0.5f+pulse[3].z);
		pos[4] = new PVector(r.x*0.5f+pulse[4].x, -r.y*0.5f+pulse[4].y, r.z*0.5f+pulse[4].z);
		pos[5] = new PVector(-r.x*0.5f+pulse[5].x, -r.y*0.5f+pulse[5].y, r.z*0.5f+pulse[5].z);
		pos[6] = new PVector(-r.x*0.5f+pulse[6].x, -r.y*0.5f+pulse[6].y, -r.z*0.5f+pulse[6].z);
		pos[7] = new PVector(r.x*0.5f+pulse[7].x, -r.y*0.5f+pulse[7].y, -r.z*0.5f+pulse[7].z);
		
		
		if(canShowEdges){
			for(int i = 0; i < pos.length-1; i++){
				p.line(pos[i].x, pos[i].y, pos[i].z, pos[i+1].x, pos[i+1].y, pos[i+1].z);
			}
			
			
		}

//		drawFaces(pos);
		
		p.line(pos[0].x, pos[0].y, pos[0].z, pos[3].x, pos[3].y, pos[3].z);
		p.line(pos[0].x, pos[0].y, pos[0].z, pos[7].x, pos[7].y, pos[7].z);
		p.line(pos[5].x, pos[5].y, pos[5].z, pos[2].x, pos[2].y, pos[2].z);
		p.line(pos[6].x, pos[6].y, pos[6].z, pos[1].x, pos[1].y, pos[1].z);
		p.line(pos[7].x, pos[7].y, pos[7].z, pos[4].x, pos[4].y, pos[4].z);
		
//		p.textSize(24);
//		p.fill(255);
//		for(int i = 0; i < pos.length; i++){
//			p.text(""+i, pos[i].x, pos[i].y, pos[i].z);
//		}
		
		if(canShowDiagonals)
			drawDiagonals(pos);
		
		if(type == 1){
			for(int i = 0; i < pos.length; i++){
				pos_abs[i] = new PVector(p.modelX(pos[i].x, pos[i].y, pos[i].z), p.modelY(pos[i].x, pos[i].y, pos[i].z), p.modelZ(pos[i].x, pos[i].y, pos[i].z));
			}
		}
			
	}
	
	void drawFaces(PVector[] pos){
		p.fill(0);
		p.beginShape();
		p.vertex(pos[0].x, pos[0].y, pos[0].z);
		p.vertex(pos[3].x, pos[3].y, pos[3].z);
		p.vertex(pos[4].x, pos[4].y, pos[4].z);
		p.vertex(pos[7].x, pos[7].y, pos[7].z);
		p.endShape(PApplet.CLOSE);
		
		p.beginShape();
		p.vertex(pos[0].x, pos[0].y, pos[0].z);
		p.vertex(pos[1].x, pos[1].y, pos[1].z);
		p.vertex(pos[6].x, pos[6].y, pos[6].z);
		p.vertex(pos[7].x, pos[7].y, pos[7].z);
		p.endShape(PApplet.CLOSE);
		
		p.beginShape();
		p.vertex(pos[2].x, pos[2].y, pos[2].z);
		p.vertex(pos[3].x, pos[3].y, pos[3].z);
		p.vertex(pos[4].x, pos[4].y, pos[4].z);
		p.vertex(pos[5].x, pos[5].y, pos[5].z);
		p.endShape(PApplet.CLOSE);
		
		p.beginShape();
		p.vertex(pos[6].x, pos[6].y, pos[6].z);
		p.vertex(pos[5].x, pos[5].y, pos[5].z);
		p.vertex(pos[2].x, pos[2].y, pos[2].z);
		p.vertex(pos[1].x, pos[1].y, pos[1].z);
		p.endShape(PApplet.CLOSE);
		
		p.beginShape();
		p.vertex(pos[6].x, pos[6].y, pos[6].z);
		p.vertex(pos[5].x, pos[5].y, pos[5].z);
		p.vertex(pos[4].x, pos[4].y, pos[4].z);
		p.vertex(pos[7].x, pos[7].y, pos[7].z);
		p.endShape(PApplet.CLOSE);
		

		p.beginShape();
		p.vertex(pos[0].x, pos[0].y, pos[0].z);
		p.vertex(pos[1].x, pos[1].y, pos[1].z);
		p.vertex(pos[2].x, pos[2].y, pos[2].z);
		p.vertex(pos[3].x, pos[3].y, pos[3].z);
		p.endShape(PApplet.CLOSE);
	}
	
	void drawCircles(PVector r){
		p.strokeWeight(1);
		p.stroke(255, circles_alpha);
		int steps = 32;
		p.pushMatrix();
		for(int i = 0; i < steps; i++){
			p.translate(p.random(-1f, 1f)*fizzleRate, p.random(-1f, 1f)*fizzleRate, p.random(-1f, 1f)*fizzleRate);
			if(i % 2 == 0)
				p.arc(0, 0, r.y*0.95f, r.y*0.95f, i*(PApplet.TWO_PI/steps), (i+1)*(PApplet.TWO_PI/steps));
		}
		p.popMatrix();
		
		p.pushMatrix();
		p.rotateX(PApplet.PI*0.5f*PApplet.map(r.z, 0, r.x, 0, 1));
		for(int i = 0; i < steps; i++){
			p.translate(p.random(-1f, 1f)*fizzleRate, p.random(-1f, 1f)*fizzleRate, p.random(-1f, 1f)*fizzleRate);
			if(i % 4 == 0)
				p.arc(0, 0, r.y*0.95f, r.y*0.95f, i*(PApplet.TWO_PI/steps), (i+1)*(PApplet.TWO_PI/steps));
		}
		p.popMatrix();
		
		p.pushMatrix();
		p.rotateX(PApplet.PI*0.5f*PApplet.map(r.z, 0, r.x, 0, 1));
		p.rotateY(PApplet.PI*0.5f*PApplet.map(r.z, 0, r.x, 0, 1));
		for(int i = 0; i < steps; i++){
			p.translate(p.random(-1f, 1f)*fizzleRate, p.random(-1f, 1f)*fizzleRate, p.random(-1f, 1f)*fizzleRate);
			p.arc(0, 0, r.y*0.95f, r.y*0.95f, i*(PApplet.TWO_PI/steps), (i+1)*(PApplet.TWO_PI/steps));
		}
		p.popMatrix();
	}
	
	void drawDiagonals(PVector[] pos){
		p.stroke(255, cube_alpha);
		for(int i = 0; i < pos.length; i++){
			for(int j = 0; j < pos.length-1; j++){
				p.translate(p.random(-1f, 1f)*fizzleRate, p.random(-1f, 1f)*fizzleRate, p.random(-1f, 1f)*fizzleRate);
				for(float k = 0; k < 10; k++){
					if(p.noise(p.millis()*0.0001f, k) > 0.5f)
						p.line(PApplet.lerp(pos[i].x, pos[j].x, k*0.1f), PApplet.lerp(pos[i].y, pos[j].y, k*0.1f), PApplet.lerp(pos[i].z, pos[j].z, k*0.1f), PApplet.lerp(pos[i].x, pos[j].x, (k+1)*0.1f), PApplet.lerp(pos[i].y, pos[j].y, (k+1)*0.1f), PApplet.lerp(pos[i].z, pos[j].z, (k+1)*0.1f));
				}

			}
		}	
	}
}
