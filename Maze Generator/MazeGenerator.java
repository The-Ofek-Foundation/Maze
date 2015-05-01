/**
 * This program makes a 2d maze using DFS
 * @author  Ofek Gila
 * @since 	April 22nd, 2015
 */
public class MazeGenerator	{
	public static void main(String[] pumpkins) {
		printMaze(generateMaze(100, 100, 10, 10));
	}
	public static void printMaze(boolean[][] maze)	{
		for (int i = 0; i < maze.length; i++)	{
			for (int a = 0; a < maze[i].length; a++)
				System.out.print(maze[i][a] ? "*":"|");
			System.out.println();
		}
	}
	public static boolean[][] generateMaze(int width, int height)	{
		return generateMaze(width, height, 0, 0);
	}
	public static boolean[][] generateMaze(int width, int height, int startX, int startY)	{
		boolean[][] maze = new boolean[width+2][height+2];
		maze[startX][startY] = true;
		generateMaze(maze, startX, startY);
		boolean[][] realmaze = new boolean[width][height];
		for (int i = 0; i < realmaze.length; i++)
			for (int a = 0; a < realmaze[i].length; a++)
				realmaze[i][a] = maze[i+1][a+1];
		return realmaze;
	}
	public static void generateMaze(boolean[][] maze, int x, int y)	{
		int[] tempRandDs = getRanDs();
		for (int i = 0; i < tempRandDs.length; i++)	{
			switch (tempRandDs[i])	{
				case 0:	{
					if (y - 2 <= 0) continue;
					if (maze[x][y-2] == false)	{
						maze[x][y-1] = true;
						maze[x][y-2] = true;
						try {	generateMaze(maze, x, y-2);	}
						catch (StackOverflowError e)	{
							generateMaze(maze, x, y-2);
						}
					}
					break;
				}	
				case 1:	{
					if (y + 2 >= maze[0].length - 1) continue;
					if (maze[x][y+2] == false)	{
						maze[x][y+1] = true;
						maze[x][y+2] = true;
						try 	{	generateMaze(maze, x, y+2);	}
						catch (StackOverflowError e)	{								
							generateMaze(maze, x, y+2);
						}
					}
					break;
				}
				case 2:	{
					if (x + 2 >= maze.length - 1) continue;
					if (maze[x+2][y] == false)	{
						maze[x+1][y] = true;
						maze[x+2][y] = true;
						try {	generateMaze(maze, x+2, y);	}
						catch (StackOverflowError e)	{
							generateMaze(maze, x+2, y);
						}
					}
					break;
				}
				case 3:	{
					if (x - 2 <= 0) continue;
					if (maze[x-2][y] == false)	{
						maze[x-1][y] = true;
						maze[x-2][y] = true;
						try {	generateMaze(maze, x-2, y);	}
						catch (StackOverflowError e)	{
							generateMaze(maze, x-2, y);
						}
					}
					break;
				}
			}
		}
	}
	public static int[] getRanDs()	{
		int[] temp = new int[4];
		for (int i = 0; i < temp.length; i++)
			temp[i] = -1;
		int ran = 0;
		for (int i = 0; i < temp.length; i++)	{
			do {
				ran = (int)(Math.random() * temp.length);
			}	while (temp[ran] != -1);
			temp[ran] = i;
		}
		return temp;
	}
}