package vertices;

import processing.core.PApplet;
import processing.core.PVector;

public class World {
	
	PApplet p;
	PVector pos;
	float rad;
	float alpha;
	int det = 100;
	float sw = 1;
	
	PVector theta;
	PVector theta_inc;
	
	float closing = 0;
	float finalrad;
	
	World(){}
	
	World(PApplet _p){
		this.p = _p;
		this.pos = new PVector(p.width*0.5f, p.height*0.5f, 0);
		this.rad = p.width*4f;
		this.alpha = 0;
		this.theta = new PVector(0, 0, 0);
		this.theta_inc = new PVector(0, 0, 0);
	}
	
	void update(){
		p.sphereDetail(det);
		
		theta.x += theta_inc.x;
		theta.y += theta_inc.y;
		theta.x += theta_inc.z;
		finalrad = rad+(p.height*0.35f - closing);
		finalrad = PApplet.max(0, finalrad);
	}
	
	void display(){
		
		p.noFill();
		p.strokeWeight(sw);
		p.stroke(0, 0, 0, alpha);
		p.pushMatrix();
		p.translate(pos.x, pos.y, pos.z);
		p.rotateX(theta.x);
		p.rotateY(theta.y);
		p.rotateZ(theta.z);
		p.sphere(finalrad);
		p.popMatrix();
		
		p.rectMode(PApplet.CORNER);
		p.fill(0);
		p.rect(0, 0, p.width, closing);
		p.rect(0, p.height, p.width, -closing);
	}
	
	public void keyPressed(){

	}

}
