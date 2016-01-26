package vertices;

import processing.core.PApplet;
import themidibus.MidiBus;


public class Vertices extends PApplet {

	MidiBus midi_beatStep;
	MidiBus midi_kontrol;

	int channel;
	int pitch;
	int velocity;
	int note;
	int value;

	public void setup() {

	}

	public void settings(){
		fullScreen();
	}

	public void debug(){
		textAlign(LEFT);
		textSize(10);
		fill(100, 255, 100);
		text("framerate: "+frameRate, 10, 10);
		text("ch: "+channel, 10, 20);
		text("pitch: "+pitch, 10, 30);
		text("vel: "+velocity, 10, 40);
		text("note: "+note, 10, 50);
		text("val: "+value, 10, 60);
	}

	public void draw() {
		background(0);
		debug();
	}

	public void noteOn(int c, int p, int v, long t, String s){
		channel = c;
		pitch = p;
		velocity = v;

		if(s == "kontrol"){
			switch(p){
			case 44:
				break;
			case 45:
				break;
			case 36:
				break;
			case 50:
				break;
			case 51:
				break;
			case 42:
				break;
			case 43:
				break;
			default:
				break;
			}
		}

		if(s == "beatstep"){
			if(c == 0){
				switch(p){
				case 44:
					break;
				case 45:
					break;
				case 46:
					break;
				case 47:
					break;
				case 48:
					break;
				case 49:
					break;
				case 50:
					break;
				case 51:
					break;
				case 36://----SECOND ROW
					break;
				case 37:
					break;
				case 38:
					break;
				case 39:
					break;
				case 40:
					break;
				case 41:
					break;
				case 42:
					break;
				case 43:
					break;
				default:
					break;
				}
			}else if(c == 1){
				switch(p){
				case 44:
					break;
				case 45:
					break;
				case 46:
					break;
				case 47:
					break;
				case 48:
					break;
				case 49:
					break;
				case 50:
					break;
				case 51:
					break;
				case 36://----SECOND ROW
					break;
				case 37:
					break;
				case 38:
					break;
				case 39:
					break;
				case 40:
					break;
				case 41:
					break;
				case 42:
					break;
				case 43:

					break;
				default:
					break;
				}
			}
		}
	}

	public void controllerChange(int c, int n, int v, long t, String s){
		channel = c;
		note = n;
		value = v;

		if(s == "kontrol"){
			switch(n){
			case 0://--------------------------------FADERS

				break;
			case 1:

				break;
			case 2:

				break;
			case 3:

				break;
			case 4:

				break;
			case 5:

				break;
			case 6:

				break;
			case 7:

				break;
			case 8://--------------------------------KNOBS

				break;
			case 9:

				break;
			case 10:

				break;
			case 11:

				break;
			case 12:

				break;
			case 13:

				break;
			case 14:

				break;
			case 15:

				break;
			default:
				break;
			}
		}else if(s == "beatstep"){
			v = v - 64;//normalize

			if(n == 7){//reset stuff
				//					if(v < -1)
				//					else if(v > 1)
			}

			if(c == 0){
				switch(n){
				case 10:
					break;
				case 11:
					break;
				case 12:
					break;
				case 13:
					break;
				case 14:
					break;
				case 15:
					break;
				case 16:
					break;
				case 17:

					break;
				case 18://--------------------------------SECOND ROW
					break;
				case 19:
					break;
				case 20:
					break;
				case 21:
					break;
				case 22:
					break;
				case 23:
					break;
				case 24:
					break;
				case 25:
					break;
				default:
					break;
				}
			}else if(c == 1){ // ------------------ LINKS
				switch(n){
				case 10:
					break;
				case 11:
					break;
				case 12:
					break;
				case 13:
					break;
				case 14:
					break;
				case 15:

					break;
				case 16:

					break;
				case 17:

					break;
				case 18://--------------------------------SECOND ROW

					break;
				case 19:

					break;
				case 20:

					break;
				case 21:

					break;
				case 22:

					break;
				case 23:

					break;
				case 24:

					break;
				case 25:

					break;
				default:
					break;
				}
			}
		}
	}

	public static void main(String _args[]) {
		PApplet.main(new String[] { vertices.Vertices.class.getName() });
	}
}
