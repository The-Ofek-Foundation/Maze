/*	Ofek Gila
	February 3rd, 2014
	ColorPicker.java
	This program will attempt to let the user get RGB values for a color
*/

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import javax.imageio.*;
import java.awt.*;			// Imports
import java.awt.event.*;
import javax.swing.*;
import java.util.Scanner;
import javax.imageio.*;
import javax.imageio.stream.*;
import javax.imageio.metadata.*;
import java.lang.Object;
import java.util.Iterator;

public class Maze	extends JApplet{			// I'm pretty sure I copied down one of your online codes for key and focus listeners for their methods
	public static void main(String[] pumpkins) {	// when I made snake.java, and I copied snake.java to have all the implements for this code, so don't
		JFrame frame = new JFrame("Maze");	// ask why I extend JApplet or implement all of those things ^_^
		if (pumpkins.length == 1)
			frame.setContentPane(new ContentPanel(Integer.parseInt(pumpkins[0])));
		else frame.setContentPane(new ContentPanel());
		frame.setSize(1000 + 18, 1000 + 47);		// Sets size of frame
		//frame.setResizable(false);						// Makes it so you can't resize the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	public void init()	{
		setContentPane(	new ContentPanel());
	}
}
class ContentPanel extends JPanel implements ActionListener, KeyListener, FocusListener, MouseListener, MouseMotionListener	{

	public final int setWidth = 1000, setHeight = 1000;
	public int wallSize = 10;
	public char[][] maze;
	public char[][][] mazes;
	public int countMaze;
	public int width, height;							// width and height of frame
	public Graphics g;									// Graphics of frame
	public Color c;
	public boolean initial = true;
	public Timer t = new Timer(50, this);
	public boolean onePath;
	public boolean[][] correctPath;
	public boolean[][] visited;
	public boolean[][] path;
	public boolean[][] deadEnd;
	public int[][] AIPath;
	public int cM;
	public int ran1, ran2, ranD;
	public int x = 5, y = 5;
	public boolean animation = false;
	public int pX, pY;
	public double startTime;
	public int mD, count;
	public boolean AI = false;
	public Rectangle AIRect;
	public Rectangle Animation;
	public int eD, eD2;
	public boolean dragging;
	public int erase;
	public int startX, startY;
	public boolean vertical = false;
	public boolean horizontal = false;
	public boolean checkerboard = false;
	public boolean inwardX = false;
	public boolean squares = false;
	public boolean awkwardCircle = false;
	public boolean circleSquare;
	public int type = BufferedImage.TYPE_INT_ARGB;
	public BufferedImage image;
	public BufferedImage[] images;
	public boolean gif = false;
	public int AIX, AIY;
	public int countTimer;
	public boolean hide = false;

	
	public ContentPanel()	{
		addKeyListener(this);							// implements the implements
		addFocusListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	public ContentPanel(int wallsize)	{
		addKeyListener(this);							// implements the implements
		addFocusListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		this.wallSize = wallsize;
	}
	public void constructor()	{
		if (Math.random() * 2 < 1) circleSquare = false;
		else circleSquare = true;
		erase = 'i';
		dragging = false;
		AIRect = new Rectangle(width - 25 * 4, 5, 25 * 3, 25 * 2 - 10);
		Animation = new Rectangle(width - 25 * 8, 5, 25 * 3, 25 * 2 - 10);
		pX = pY = 2;
		countMaze = countTimer = 0;
		if (width / wallSize % 2 == 1) eD = 3;
		else eD = 2;
		if (height / wallSize % 2 == 1) eD2 = 3;
		else eD2 = 2;
		startX = (int)(Math.random() * (width / (2 * wallSize + 4)) + 1) * 2;
		startY = (int)(Math.random() * (height / (2 * wallSize + 4)) + 1) * 2;
		//System.out.println(startX + " " + (width / wallSize));
		maze = new char[width/wallSize][height/wallSize];
		correctPath = new boolean[width/wallSize][height/wallSize];		
		visited = new boolean[width/wallSize][height/wallSize];	
		path = new boolean[width/wallSize][height/wallSize];	
		deadEnd = new boolean[width/wallSize][height/wallSize];
		AIPath = new int[width/wallSize][height/wallSize];
		onePath = false;
		for (int row = 0; row < maze.length; row++)
			for (int col = 0; col < maze[row].length; col++)	{
				AIPath[row][col] = maze.length * maze[row].length;
				maze[row][col] = '|';
				correctPath[row][col] = false;
				visited[row][col] = false;
				path[row][col] = false;
				deadEnd[row][col] = false;
			}
		maze[startX][startY] = '-';
	}
	public int getRelativeX(double x)	{
		return (int)((x / width) * setWidth + 0.5);
	}
	public int getRelativeY(double y)	{
		return (int)((y / height) * setHeight + 0.5);
	}
	public void paintComponent(Graphics a)	{
		super.paintComponent(a);
		g = a;
		width = getWidth();
		height = getHeight();	
		//System.out.println(width + " " + height);
		if (initial)	{
			while (ran1 > 0 && ran1 < width/wallSize - 1 && ran2 > 0 && ran2 < height / wallSize - 1)	{
				ran1 = (int)(Math.random() * (width / wallSize));
				ran2 = (int)(Math.random() * (height / wallSize));
				System.out.println((width / wallSize));
			}
			
			initial = false;
			constructor();
			AIX = width / wallSize - eD;
			AIY = height / wallSize - eD2;
			if (squares || (awkwardCircle && circleSquare)) {
				AIX = maze.length / 2;
				AIY = maze[AIX].length / 2;
			}
			//System.out.println(maze.length);
			generateMaze(startX, startY);
			if (!(animation)) t.stop();
			if (squares || (awkwardCircle && circleSquare)) maze[width / wallSize / 2][height / wallSize / 2] = 'E';
			else maze[width / wallSize - eD][height / wallSize - eD2] = 'E';
			//createWalls();
			//createPaths(50);
			//depthFirstSearch(5, 5);
		}
		//drawWalls();
		//if (!(mazePossible(0, 0, maze)))	{	initial = true;	repaint();	}
		//else if (mazeUnique(0, 0, maze))	drawWalls();
		//else {
		//	initial = true;
		//	repaint();
		//	//drawWalls();
		//}
		drawWalls();
	}
	public void storeMaze(char[][] maze)	{
		for (int row = 0; row < maze.length; row++)
			for (int col = 0; col < maze[row].length; col++)
				mazes[countMaze][row][col] = maze[row][col];
//		mazes[countMaze] = maze;
		countMaze++;
	}
	public void generateMaze(int x, int y)	{
		int[] tempRandDs = new int[4];
		tempRandDs = getRandDs(x, y);
		//System.out.println(maze[x][y]);
		//System.out.println(countMaze);
		/*if (countMaze > 0)
		for (int row = 0; row < 10; row++)	{
			for (int col = 0; col < 10; col++)
				System.out.print(mazes[countMaze-1][row][col] + " ");
			System.out.println();
		}
		if (countMaze > 10) System.exit(0);*/
		for (int i = 0; i < tempRandDs.length; i++)	{
			switch (tempRandDs[i])	{
				case 0:	{
					if (y - 2 <= 0) continue;
					if (maze[x][y-2] == '|')	{
						maze[x][y-1] = '-';
						maze[x][y-2] = '-';
						if (animation)	storeMaze(maze);
						try {	generateMaze(x, y-2);	}
						catch (StackOverflowError e)	{
							//for (double d = System.nanoTime(); (System.nanoTime() - d)/1E4 < 1; d=d);
							//System.out.println("1");
							generateMaze(x, y-2);
						}
					}
					break;
				}	
				case 1:	{
					if (y + 2 >= height / wallSize - 1) continue;
					if (maze[x][y+2] == '|')	{
						maze[x][y+1] = '-';
						maze[x][y+2] = '-';
						if (animation)	storeMaze(maze);
						try 	{	generateMaze(x, y+2);	}
						catch (StackOverflowError e)	{
							//for (double d = System.nanoTime(); (System.nanoTime() - d)/1E4 < 1; d=d);
							//System.out.println("3");								
							generateMaze(x, y+2);
						}
					}
					break;
				}
				case 2:	{
					if (x + 2 >= width / wallSize - 1) continue;
					if (maze[x+2][y] == '|')	{
						maze[x+1][y] = '-';
						maze[x+2][y] = '-';
						if (animation)	storeMaze(maze);
						try {	generateMaze(x+2, y);	}
						catch (StackOverflowError e)	{
							//for (double d = System.nanoTime(); (System.nanoTime() - d)/1E4 < 1; d=d);
							//System.out.println("2");
							generateMaze(x+2, y);
						}
					}
					break;
				}
				case 3:	{
					if (x - 2 <= 0) continue;
					if (maze[x-2][y] == '|')	{
						maze[x-1][y] = '-';
						maze[x-2][y] = '-';
						if (animation)	storeMaze(maze);
						try {	generateMaze(x-2, y);	}
						catch (StackOverflowError e)	{
							//for (double d = System.nanoTime(); (System.nanoTime() - d)/1E4 < 1; d=d);
							//System.out.println("4");
							generateMaze(x-2, y);
						}
					}
					break;
				}
			}
		}
		if (animation)	{
			maze[x][y] = 'C';
			if (maze[x+1][y] == '-') maze[x+1][y] = 'C';
			if (maze[x-1][y] == '-') maze[x-1][y] = 'C';
			if (maze[x][y+1] == '-') maze[x][y+1] = 'C';
			if (maze[x][y-1] == '-') maze[x][y-1] = 'C';
			storeMaze(maze);
		}
	}
	public int[] getRandDs(int x, int y)	{
		int[] temp = new int[4];
		int ran;
		for (int i = 0; i < 4; i++)	
			temp[i] = -1;
		for (int a = 0; a < 4; a++)	{
			do {
				if (vertical && Math.random() * 10 < 8) ran = (int)(Math.random() * 2);
				else if (horizontal && Math.random() * 10 < 8)	ran = (int)(Math.random() * 2) + 2;
				else if (checkerboard)	{
					if (checkerboardVertical(x, y) && Math.random() * 10 < 9) ran = (int)(Math.random() * 2);
					else if (Math.random() * 10 < 9) ran = (int)(Math.random() * 2) + 2;
					else ran = (int)(Math.random() * 4);
				}
				else if (inwardX)	{
					if (inwardXVertical(x, y) && Math.random() * 10 < 9) ran = (int)(Math.random() * 2);
					else if (Math.random() * 10 < 9) ran = (int)(Math.random() * 2) + 2;
					else ran = (int)(Math.random() * 4);
				}
				else if (squares)	{
					if (squareVertical(x, y) && Math.random() * 20 < 19) ran = (int)(Math.random() * 2);
					else if (Math.random() * 20 < 19) ran = (int)(Math.random() * 2) + 2;
					else ran = (int)(Math.random() * 4);
				}
				else if (awkwardCircle)	{
					if (((circleSquare && awkwardCircleVertical(x, y)) || (!(circleSquare) && !(awkwardCircleVertical(x, y)))) && Math.random() * 20 < 19) ran = (int)(Math.random() * 2);
					else if (Math.random() * 20 < 19) ran = (int)(Math.random() * 2) + 2;
					else ran = (int)(Math.random() * 4);
				}
				else ran = (int)(Math.random() * 4);
			} while (temp[ran] != -1);
			temp[ran] = a;
		}
		return temp;
	}
	public boolean awkwardCircleVertical(int x, int y)	{
		double deltaX = Math.abs(x - maze.length / 2);
		double deltaY = Math.abs(y - maze[x].length / 2);
		double delta = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
		if (delta < maze.length / 2) {
			if (getRelativeX(x * wallSize) / wallSize < getRelativeY(y * wallSize) / wallSize && getRelativeX(x * wallSize) / wallSize > maze.length - getRelativeY(y * wallSize) / wallSize) return false;
			if (getRelativeX(x * wallSize) / wallSize > getRelativeY(y * wallSize) / wallSize && getRelativeX(x * wallSize) / wallSize < maze.length - getRelativeY(y * wallSize) / wallSize) return false;
			if (getRelativeX(x * wallSize) / wallSize < getRelativeY(y * wallSize) / wallSize && getRelativeX(x * wallSize) / wallSize < maze.length - getRelativeY(y * wallSize) / wallSize) return true;
			if (getRelativeX(x * wallSize) / wallSize > getRelativeY(y * wallSize) / wallSize && getRelativeX(x * wallSize) / wallSize > maze.length - getRelativeY(y * wallSize) / wallSize) return true;
		}
		if (getRelativeX(x * wallSize) / wallSize < getRelativeY(y * wallSize) / wallSize && getRelativeX(x * wallSize) / wallSize > maze.length - getRelativeY(y * wallSize) / wallSize) return true;
		if (getRelativeX(x * wallSize) / wallSize > getRelativeY(y * wallSize) / wallSize && getRelativeX(x * wallSize) / wallSize < maze.length - getRelativeY(y * wallSize) / wallSize) return true;
		if (getRelativeX(x * wallSize) / wallSize < getRelativeY(y * wallSize) / wallSize && getRelativeX(x * wallSize) / wallSize < maze.length - getRelativeY(y * wallSize) / wallSize) return false;
		if (getRelativeX(x * wallSize) / wallSize > getRelativeY(y * wallSize) / wallSize && getRelativeX(x * wallSize) / wallSize > maze.length - getRelativeY(y * wallSize) / wallSize) return false;

		return false;
	}
	public boolean inwardXVertical(int x, int y)	{
		if (getRelativeX(x * wallSize) / wallSize < getRelativeY(y * wallSize) / wallSize && getRelativeX(x * wallSize) / wallSize > maze.length - getRelativeY(y * wallSize) / wallSize) return true;
		if (getRelativeX(x * wallSize) / wallSize > getRelativeY(y * wallSize) / wallSize && getRelativeX(x * wallSize) / wallSize < maze.length - getRelativeY(y * wallSize) / wallSize) return true;
		return false;
	}
	public boolean squareVertical(int x, int y)	{
		if (getRelativeX(x * wallSize) / wallSize < getRelativeY(y * wallSize) / wallSize && getRelativeX(x * wallSize) / wallSize > maze.length - getRelativeY(y * wallSize) / wallSize) return false;
		if (getRelativeX(x * wallSize) / wallSize > getRelativeY(y * wallSize) / wallSize && getRelativeX(x * wallSize) / wallSize < maze.length - getRelativeY(y * wallSize) / wallSize) return false;
		return true;
	}
	public boolean checkerboardVertical(int x, int y)	{
		if (getRelativeX(x * wallSize) / wallSize / (maze.length / 5) % 2 == 0 && getRelativeY(y * wallSize) / wallSize / (maze[x].length / 5) % 2 == 0) return true;
		//if (getRelativeX(x * wallSize) / wallSize / (maze.length / 5) % 2 == 1 && getRelativeY(y * wallSize) / wallSize / (maze[getRelativeX(x * wallSize) / wallSize].length / 5) % 2 == 0) return false;
		//if (x / (maze.length / 5) % 2 == 0 && getRelativeY(y * wallSize) / wallSize / (maze[x].length / 5) % 2 == 1) return false;
		if (getRelativeX(x * wallSize) / wallSize / (maze.length / 5) % 2 == 1 && getRelativeY(y * wallSize) / wallSize / (maze[x].length / 5) % 2 == 1) return true;
		return false;
	}
	public void addDFS()	{
		for (int row = 0; row < maze.length; row++)
			for (int col = 0; col < maze[row].length; col++) {
				if (visited[row][col])
					maze[row][col] = 'V';
				if (path[row][col])
					maze[row][col] = '-';
			}
	}
	public void depthFirstSearch(int x, int y)	{
		//System.out.println(x + " " + y);
		path[x][y] = true;
		if (!(visited[x-1][y] && visited[x+1][y] && visited[x][y-1] && visited[x][y+1]))	{
			visited[x][y] = visited[x-1][y] = visited[x+1][y] = visited[x][y-1] = visited[x][y+1] = true;
			while (true)	{
				ranD = (int)(Math.random() * 4);
				if (ranD == 0 && !(visited[x][y-1])) {	y--;	break;	}
				if (ranD == 1 && !(visited[x+1][y])) {	x++;	break;	}
				if (ranD == 2 && !(visited[x][y+1])) {	y++;	break;	}
				if (ranD == 3 && !(visited[x-1][y])) {	x--;	break;	}
			}
		}
		else	{
			deadEnd[x][y] = true;
			if (path[x][y-1] && !(deadEnd[x][y-1])) y--;
			else if (path[x+1][y] && !(deadEnd[x+1][y])) x++;
			else if (path[x][y+1] && !(deadEnd[x][y+1])) y++;
			else if (path[x-1][y] && !(deadEnd[x-1][y])) x--;
		}
		//addDFS();
		//drawWalls();
		if (!(deadEnd[x][y])) depthFirstSearch(x, y);
	}
	public void createPaths(int times)	{
		int ran1, ran2, ranD;
		ran1 = (int)(Math.random() * (width / wallSize));
		ran2 = (int)(Math.random() * (height / wallSize));
		ranD = (int)(Math.random() * 4);
		while (ran1 > -1 && ran1 < width/wallSize && ran2 > -1 && ran2 < height / wallSize)	{
			if (times % 2 == 0) maze[ran1][ran2] = '|';
			else maze[ran1][ran2] = '-';
			switch (ranD)	{
				case 0: ran2--; break;
				case 1: ran1++; break;
				case 2: ran2++; break;
				case 3: ran1--; break;
			}
		}
		if (times != 0) createPaths(times - 1);
	}
	public void createWalls()	{
		for (int row = 0; row < maze.length; row++)
			for (int col = 0; col < maze[row].length; col++)	
				if (Math.random() * 2 < 1) maze[row][col] = '|';
		maze[0][0] = 'S';
		maze[width/wallSize - 1][height/wallSize - 1] = 'E';
	}
	public void drawWalls()	{
		if (!(hide))	{
			g.setColor(Color.green);
			g.fillRect((int)(AIRect.getX()),(int)(AIRect.getY()), (int)(AIRect.getWidth()), (int)(AIRect.getHeight()));
			g.fillRect((int)(Animation.getX()),(int)(Animation.getY()), (int)(Animation.getWidth()), (int)(Animation.getHeight()));
		}
		//System.out.println("here");
		g.setColor(Color.black);
		for (int row = 0; row < maze.length; row++)	{
			for (int col = 0; col < maze[row].length; col++)	{
				//System.out.print(maze[row][col] + " ");
				if (maze[row][col] == '|') g.fillRect(row * wallSize, col * wallSize, wallSize, wallSize);
				if (correctPath[row][col])	{
					g.setColor(Color.magenta);
					g.fillRect(row * wallSize, col * wallSize, wallSize, wallSize);
				}
				if (maze[row][col] == 'C') 	{	
					g.setColor(Color.blue);
					g.fillRect(row * wallSize, col * wallSize, wallSize, wallSize);
				}
				if (maze[row][col] == 'E')	{
					g.setColor(Color.red);
					g.fillRect(row * wallSize, col * wallSize, wallSize, wallSize);
				}
				g.setColor(Color.black);
			}
			//System.out.println();
		}
	//	g.setColor(Color.red);
	//	g.fillRect((maze.length - eD) * wallSize, (maze[0].length - eD2) * wallSize, wallSize, wallSize);
		g.setColor(Color.orange);
		g.fillRect(pX * wallSize, pY * wallSize, wallSize, wallSize);
		g.setColor(Color.blue);
		if (AI) g.fillRect(AIX * wallSize, AIY * wallSize, wallSize, wallSize);

		if (!(hide)) {
			g.setFont(new Font("Arial", Font.ITALIC, 50));
			g.setColor(Color.red);
			g.drawString("Time: " + (System.nanoTime() - startTime) / 1E9, 5, 15 + 25);
			g.setFont(new Font("Arial", Font.BOLD, 35));
			g.drawString("AI", width - 25 * 3 - 5, 25 + 10);
			g.drawString("A-n", width - 25 * 6 - 40, 25 + 10);
		}
	}
	public boolean mazePossible(int row, int col, char[][] maze)	{
		if (maze[row][col] == 'E')	return true;
		if (maze[row][col] == '*' || maze[row][col] == '|') return false;
		maze[row][col] = '*';
		if (row != 0)	
			if (mazePossible(row - 1, col, maze)) {
				if (correctPath[row][col]) correctPath[row][col] = false;
				else correctPath[row][col] = true;
				maze[row][col] = '-';
				return true;
			}
		if (row != width / wallSize - 1)
			if (mazePossible(row + 1, col, maze)) {
				if (correctPath[row][col]) correctPath[row][col] = false;
				else correctPath[row][col] = true;
				maze[row][col] = '-';
				return true;
			}
		if (col != 0) 
			if (mazePossible(row, col - 1, maze)) {
				if (correctPath[row][col]) correctPath[row][col] = false;
				else correctPath[row][col] = true;
				maze[row][col] = '-';
				return  true;
			}
		if (col != height / wallSize - 1)
			if (mazePossible(row, col + 1, maze)) {
				if (correctPath[row][col]) correctPath[row][col] = false;
				else correctPath[row][col] = true;
				maze[row][col] = '-';
				return true;
			}
		return false;
	}
	public boolean mazeUnique(int row, int col, char[][] maze)	{
		if (row == width / wallSize - 1 && col == height / wallSize - 1)	return true;
		if (maze[row][col] == '*' || maze[row][col] == '|') return false;
		maze[row][col] = '*';
		if (col != height / wallSize - 1)
			if (mazeUnique(row, col + 1, maze))	{
				if (!(correctPath[row][col])) return false;
				return true;
			}
		if (col != 0) 
			if (mazeUnique(row, col - 1, maze)) {
				if (!(correctPath[row][col])) return false;
				return  true;
			}
		if (row != width / wallSize - 1)
			if (mazeUnique(row + 1, col, maze)) {
				if (!(correctPath[row][col])) return false;
				return true;
			}
		if (row != 0)	
			if (mazeUnique(row - 1, col, maze)) {
				if (!(correctPath[row][col])) return false;
				return true;
			}
		return false;
	}
	public void mouseDragged(MouseEvent evt)	{
		if (!(dragging)) return;
		int r, c;
		r = evt.getX() / wallSize;
		c = evt.getY() / wallSize;
		if (maze[r][c] == '-' && erase == 'n')
			maze[r][c] = 'C';
		else if (maze[r][c] == 'C' && erase == 'y')
			maze[r][c] = '-';
		if (correctTouching())	{
			if (correctPath()) {
				t.stop();
				repaint();
				System.out.println("done");
			}
		}
		//else System.out.println("wrong");
	}
	public boolean correctPath()	{
		int row = 2; int col = 2;
		boolean[][] newLand = new boolean[width / wallSize][height / wallSize];
		for (int r = 0; r < maze.length; r++)
			for (int c = 0; c < maze[r].length; c++)
				newLand[r][c] = true;
		while (maze[row][col] != 'E')	{
			//System.out.println(maze[row][col]);
			//System.out.println(row + " " + col);
			if ((maze[row+1][col] == 'C' || maze[row+1][col] == 'E') && newLand[row+1][col])
				row++;
			else if ((maze[row-1][col] == 'C' || maze[row-1][col] == 'E') && newLand[row-1][col])
				row--;
			else if ((maze[row][col+1] == 'C' || maze[row][col+1] == 'E') && newLand[row][col+1])
				col++;
			else if ((maze[row][col-1] == 'C' || maze[row][col-1] == 'E') && newLand[row][col-1])
				col--;
			else return false;
			newLand[row][col] = false;
		}
		for (int r = 0; r < maze.length; r++)
			for (int c = 0; c < maze[r].length; c++)
				if (maze[r][c] == 'C')
					if (newLand[r][c])
						return false;
		return true;
	}
	public boolean correctTouching()	{
		for (int row = 0; row < maze.length; row++)
			for (int col = 0; col < maze[row].length; col++)
				if (maze[row][col] == 'C')
					if (touchingTooMuch(row, col))
						return false;
		return true;
	}
	public boolean touchingTooMuch(int row, int col)	{
		int countTouches = 0;
		if (maze[row+1][col] == 'C') countTouches++;
		if (maze[row-1][col] == 'C') countTouches++;
		if (maze[row][col+1] == 'C') countTouches++;
		if (maze[row][col-1] == 'C') countTouches++;
		if (countTouches < 1 || countTouches > 2)
			return true;
		return false;
	}
	public void mouseMoved(MouseEvent evt)	{	}
	public void setVisited()	{
		visited[x][y] = true;
		if (y != 0) visited[x][y-1] = true;
		if (x != 0) visited[x-1][y] = true;
		if (y != height / wallSize - 1) visited[x][y+1] = true;
		if (x != width / wallSize - 1) visited[x+1][y] = true;
	}
	public void setUnvisited()	{
		if (y != 0) if (!(pathClose(x, y-1))) visited[x][y-1] = false;
		if (x != 0) if (!(pathClose(x-1, y))) visited[x-1][y] = false;
		if (y != height / wallSize - 1) if (!(pathClose(x, y+1))) visited[x][y+1] = false;
		if (x != width / wallSize - 1) if (!(pathClose(x+1, y))) visited[x+1][y] = false;
	}
	public boolean pathClose(int x, int y)	{
		int count = 0;
		if (path[x][y]) return true;
		if (x != 0)	if (!(path[x-1][y])) count++;
		if (x != width / wallSize - 1)	if (!(path[x+1][y])) count++;
		if (y != 0)	if (!(path[x][y-1])) count++;
		if (y != height / wallSize - 1)	if (!(path[x][y+1])) count++;
		if (count > 1) return true;
		return false;	
	}
	public void actionPerformed(ActionEvent e)	{
		if (animation)	{
			if (countMaze == cM)	{
				t.stop();
				animation = false;
				gif = false;
				for (int row = 0; row < maze.length; row++)
					for (int col = 0; col < maze[row].length; col++)
						if (maze[row][col] == 'C')
							maze[row][col] = '-';
				if (squares || (awkwardCircle && circleSquare)) maze[width / wallSize / 2][height / wallSize / 2] = 'E';
				else maze[width / wallSize - eD][height / wallSize - eD2] = 'E';
				repaint();
				return;
			}
			
			for (int row = 0; row < maze.length; row++)
				for (int col = 0; col < maze[row].length; col++)
					maze[row][col] = mazes[cM][row][col];
			if (gif) saveImage(maze);

			//maze = mazes[cM];
			cM++;
		}
		if (maze[pX][pY] == 'E' || (AIX == 2 && AIY == 2))	{
			t.stop();
			repaint();
			return;
		}
		if (AI && countTimer % 35 == 0)	{
			/*if (!(moveAI(3)))
				if (!(moveAI(0)))
					if (!(moveAI(1)))
						moveAI(2);*/
			moveAISmart();
		}
		countTimer++;
		if (pX == AIX && pY == AIY) {
			pX = 2;
			pY = 2;
		}
		repaint();
	}
	public boolean moveAI(int add)	{
		switch((mD + add) % 4)	{
			case 0: 
				if (maze[pX - 1][pY] == '|') break;
				pX--;
				mD = 0;
				return true;
			case 1:
				if (maze[pX][pY - 1] == '|') break;
				pY--;
				mD = 1;
				return true;
			case 2:
				if (maze[pX + 1][pY] == '|') break;
				pX++;
				mD = 2;
				return true;
			case 3:
				if (maze[pX][pY + 1] == '|') break;
				pY++;
				mD = 3;
				return true;
		}
		return false;
	}
	public void moveAISmart()	{
		/*if (AIPath[pX+1][pY] > AIPath[pX][pY] || maze[pX+1][pY] == 'E') pX++;
		else if (AIPath[pX-1][pY] > AIPath[pX][pY] || maze[pX-1][pY] == 'E') pX--;
		else if (AIPath[pX][pY+1] > AIPath[pX][pY] || maze[pX][pY+1] == 'E') pY++;
		else if (AIPath[pX][pY-1] > AIPath[pX][pY] || maze[pX][pY-1] == 'E') pY--;*/
		//System.out.println(AIPath[AIX][AIY] + " " + (AIPath[AIX][AIY-1]));
		if(AIX < maze.length - 1) {	if (AIPath[AIX+1][AIY] < AIPath[AIX][AIY] || (AIX + 1 == 2 && AIY == 2)) {	AIX++;	return;	}	}
		if (AIX != 0)	{	if (AIPath[AIX-1][AIY] < AIPath[AIX][AIY] || (AIX - 1 == 2 && AIY == 2))	{ AIX--;	return;	}	}
		if (AIY < maze[AIX].length - 1)	{	if (AIPath[AIX][AIY+1] < AIPath[AIX][AIY] || (AIX == 2 && AIY + 1 == 2))	{ AIY++;	return;	}	}
		if (AIY != 0)	{ if (AIPath[AIX][AIY-1] < AIPath[AIX][AIY] || (AIX == 2 && AIY - 1 == 2)) {	AIY--; return;	}	}
	}
	public void focusGained(FocusEvent evt) {	}
	public void focusLost(FocusEvent evt) {	}
	public void keyTyped(KeyEvent evt) {}
	public void keyPressed(KeyEvent evt) {	
		int key = evt.getKeyCode();
		//if (!(AI))	{
			if (key == KeyEvent.VK_LEFT && maze[pX-1][pY] != '|')
				pX--;
			else if (key == KeyEvent.VK_UP && maze[pX][pY-1] != '|')
				pY--;
			else if (key == KeyEvent.VK_DOWN && maze[pX][pY+1] != '|')
				pY++;
			else if (key == KeyEvent.VK_RIGHT && maze[pX+1][pY] != '|')
				pX++;
		//}
		if (key == '-')	wallSize--;
		if (key == '=' && evt.isShiftDown())	wallSize++;
		//System.out.println((char)key);
		if (key == KeyEvent.VK_SPACE)	{
			//animation = false;
			boolean b = getAIPath(2, 2, maze, 2);
			for (int row = 0; row < maze.length; row++)
				for (int col = 0; col < maze[row].length; col++)
					if (maze[row][col] == '*')
						maze[row][col] = '-';
			pX = 2;
			pY = 2;
			//if (!(AI))	t.setDelay(1);
			//else t.setDelay(112500 / ((750 / wallSize) * (750 / wallSize)) * 2);
			t.setDelay(1);
			startTime = System.nanoTime();
			t.start();
		}
		if (key == 'z' || key == 'Z') {
			if (hide) hide = false;
			else hide = true;
			repaint();
		}
		if (key == 'n' || key == 'N')
			resetGenType();
		if (key == 'c' || key == 'C') {
			resetGenType();
			checkerboard = true;
		}
		if (key == 'v' || key == 'V') {
			resetGenType();
			vertical = true;
		}
		if (key == 'h' || key == 'H') {
			resetGenType();
			horizontal = true;
		}
		if (key == 'x' || key == 'X') {
			resetGenType();
			inwardX = true;
		}
		if (key == 's' || key == 'S')	{
			resetGenType();
			squares = true;
		}
		if (key == 'a' || key == 'A')	{
			resetGenType();
			awkwardCircle = true;
		}
		if (key == 'g' || key == 'G')	{
			gif = true;
		}
		if (key == 'p' || key == 'P')	{
			int rgb;
			/*if (animation)	{
				image = new BufferedImage(width, height, type);
				for(int x = 0; x < width; x++)
    				for(int y = 0; y < height; y++) {
    					if (x / wallSize == width / wallSize || y / wallSize == height / wallSize)	rgb = new Color(0, 0, 0).getRGB();
    					else if (x / wallSize == pX && y / wallSize == pY)	rgb = new Color(255, 140, 0).getRGB();
    					else if (correctPath[x / wallSize][y / wallSize])	rgb = new Color (0, 255, 0).getRGB();    					
    					else if (maze[x / wallSize][y / wallSize] == '|')	rgb = new Color(0, 0, 0).getRGB();
    					else if (maze[x / wallSize][y / wallSize] == 'C')	rgb = new Color(0, 0, 255).getRGB();
    					else if (maze[x / wallSize][y / wallSize] == 'E')	rgb = new Color(255, 0, 0).getRGB();
    					else rgb = new Color(255, 255, 255).getRGB();
    				//	rgb = new Color(0, 0, 0).getRGB();
    					image.setRGB(x, y, rgb);
    				}
    			File outputfile = new File("final.png");
    			try {
					ImageIO.write(image, "png", outputfile);
    			}
    			catch (IOException e)	{
    				System.exit(5);
    			}
    			images = new BufferedImage[countMaze];
				for (int i = 0; i < mazes.length; i ++)	{
					images[i] = new BufferedImage(width, height, type);
					image = new BufferedImage(width, height, type);
					for(int x = 0; x < width; x++)
    					for(int y = 0; y < height; y++) {
    						if (x / wallSize == width / wallSize || y / wallSize == height / wallSize)	rgb = new Color(0, 0, 0).getRGB();
    						else if (x / wallSize == pX && y / wallSize == pY)	rgb = new Color(255, 140, 0).getRGB();
    						else if (correctPath[x / wallSize][y / wallSize])	rgb = new Color (0, 255, 0).getRGB();
    						else if (maze[x / wallSize][y / wallSize] == '|')	rgb = new Color(0, 0, 0).getRGB();
    						else if (maze[x / wallSize][y / wallSize] == 'C')	rgb = new Color(0, 0, 255).getRGB();
    						else if (maze[x / wallSize][y / wallSize] == 'E')	rgb = new Color(255, 0, 0).getRGB();
    						else rgb = new Color(255, 255, 255).getRGB();
    					//	rgb = new Color(0, 0, 0).getRGB();
    						images[i].setRGB(x, y, rgb);
    					}
				}
				try {
					makeGif(images);
				}
				catch (Exception e)	{
					System.exit(6);
				}
				return;
			}*/
			saveImage(maze);
		}
		//repaint();
	}
	public void saveImage(char[][] maze)	{
		int rgb;
		image = new BufferedImage(width, height, type);
		for(int x = 0; x < width; x++)
    		for(int y = 0; y < height; y++) {
    			if (x / wallSize == width / wallSize || y / wallSize == height / wallSize)	rgb = new Color(0, 0, 0).getRGB();
    			else if (x / wallSize == pX && y / wallSize == pY)	rgb = new Color(255, 140, 0).getRGB();
    			else if (correctPath[x / wallSize][y / wallSize])	rgb = new Color (0, 255, 0).getRGB();
    			else if (maze[x / wallSize][y / wallSize] == '|')	rgb = new Color(0, 0, 0).getRGB();
    			else if (maze[x / wallSize][y / wallSize] == 'C')	rgb = new Color(0, 0, 255).getRGB();
    			else if (maze[x / wallSize][y / wallSize] == 'E')	rgb = new Color(255, 0, 0).getRGB();
    			else rgb = new Color(255, 255, 255).getRGB();
    		//	rgb = new Color(0, 0, 0).getRGB();
    			image.setRGB(x, y, rgb);
    		}
    	String fileName = "Maze" + System.nanoTime();
    	File outputfile = new File(fileName + ".png");
    	try {
			ImageIO.write(image, "png", outputfile);
    	}
    	catch (IOException e)	{
    		System.exit(5);
    	}
    }
	public void resetGenType()	{
		checkerboard = false;
		vertical = false;
		horizontal = false;
		inwardX = false;
		squares = false;
		awkwardCircle = false;
	}
	public boolean getAIPath(int row, int col, char[][] maze, int count)	{
		if (maze[row][col] == 'E')	return true;
		if (maze[row][col] == '*' || maze[row][col] == '|') return false;
		maze[row][col] = '*';
		if (row != 0)	
			if (getAIPath(row - 1, col, maze, count + 1)) {
				AIPath[row][col] = count;
				return true;
			}
		if (row != width / wallSize - 1)
			if (getAIPath(row + 1, col, maze, count + 1)) {
				AIPath[row][col] = count;
				return true;
			}
		if (col != 0) 
			if (getAIPath(row, col - 1, maze, count + 1)) {
				AIPath[row][col] = count;
				return  true;
			}
		if (col != height / wallSize - 1)
			if (getAIPath(row, col + 1, maze, count + 1)) {
				AIPath[row][col] = count;
				return true;
			}
		return false;
	}
	public void keyReleased(KeyEvent evt) {	}
	public void mouseEntered(MouseEvent evt) { } 
	public void mousePressed(MouseEvent evt) {	
		int x = evt.getX();
		int y = evt.getY();
		requestFocus();
		for (int row = 0; row < maze.length; row++)
			for (int col = 0; col < maze[row].length; col++)
				visited[row][col] = false;
		//System.out.println("mouse");
		if (evt.isControlDown())	{
			if (squares || (awkwardCircle && circleSquare)) maze[width / wallSize / 2][height / wallSize / 2] = 'E';
			else maze[width / wallSize - eD][height / wallSize - eD2] = 'E';
			mazePossible(2, 2, maze);
			repaint();
			return;
		}
		if (evt.isShiftDown()) {	
			initial = true;
			t.stop();
			startTime = System.nanoTime();

		}
		if (AIRect.contains(x, y))	{	if (AI) {	AI = false; t.setDelay(1);	}	else 	{	AI = true;	t.setDelay(50);	}	}
		else if (Animation.contains(x, y)) {	if (animation) animation = false; else animation = true;	}
		else if (animation)	{
			t.stop();
			mazes = new char[(width/wallSize)*(height/wallSize)*2][width/wallSize][height/wallSize];
			initial = true;
			repaint();
			pX = 2;
			pY = 2;
			cM = 0;
			//if (AI) t.setDelay(100);
			//else t.setDelay(50);
			t.setDelay(112500 / ((750 / wallSize) * (750 / wallSize)) - 1);
			startTime = System.nanoTime();
			t.start();
		}
		else {
			int r, c;
			dragging = true; 
			if (erase == 'i')	{
				t.setDelay(1);
				startTime = System.nanoTime();
				t.start();
			}
			r = x / wallSize;
			c = y / wallSize;
			if (maze[r][c] == '-')	{
				maze[r][c] = 'C';
				erase = 'n';
			}
			else if (maze[r][c] == 'C') {
				maze[r][c] = '-';
				erase = 'y';
			}
			if (correctTouching())	{
				if (correctPath()) {
					t.stop();
					repaint();
					System.out.println("done");
				}
			}
			return;	
		}
		
		repaint();
	}
    public void mouseExited(MouseEvent evt) { } 
    public void mouseReleased(MouseEvent evt) {  } 
    public void mouseClicked(MouseEvent evt) { }
}