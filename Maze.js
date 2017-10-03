var docWidth, docHeight;
var mazeWidth, mazeHeight;
var dimensions = new Array(2);
var blockWidth;
var maze;
var startx, starty;
var mazeStyle, styleIntensity;
var startMaze = new Array(2);
var endMaze = new Array(2);
var animate;
var animation, animationOn, animationInterval;
var totalAnimationTime, customResolution;
var showStartEnd, thinMaze;
var visited, correctPath;
var deadVisited;

var mazeui = document.getElementById("maze");
var brush = mazeui.getContext("2d");

function pageReady() {
	getSettings();

	resizeMaze();
	newGame();
	setTimeout(resizeSettingsTable, 0);
};

function resizeMaze() {

	docWidth = getElemWidth(contentWrapper);
	docHeight = getElemHeight(contentWrapper);
	wrapperTop = contentWrapper.offsetTop;

	setElemStyle(getElemId('maze-tr-div'), 'top', wrapperTop + "px");

	var recBlockWidth = docWidth / dimensions[0] < docHeight / dimensions[1] ? (docWidth / dimensions[0]):(docHeight / dimensions[1]);
	if (!customResolution)
		blockWidth = (docWidth / dimensions[0] < docHeight / dimensions[1] ? (docWidth / dimensions[0] | 0):(docHeight / dimensions[1] | 0));

	mazeWidth = blockWidth * dimensions[0];
	mazeHeight = blockWidth * dimensions[1];
	var recWidth = recBlockWidth * dimensions[0];
	var recHeight = recBlockWidth * dimensions[1];

	setElemWidth(mazeui, recWidth);
	setElemHeight(mazeui, recWidth);
	setElemStyle(mazeui, 'left', (docWidth - recWidth) / 2 + "px")
	setElemStyle(mazeui, 'top', (docHeight - recHeight) / 2 + "px")
	mazeui.setAttribute('width', mazeWidth);
	mazeui.setAttribute('height', mazeHeight);

	resizeSettingsTable();
}

function onResize() {
	resizeMaze();
	drawMaze();
}

function clearMaze() {
	brush.clearRect(0, 0, mazeWidth, mazeHeight);
	brush.beginPath();
	brush.rect(0, 0, mazeWidth, mazeHeight);
	brush.fillStyle = "white";
	brush.fill();
	brush.closePath();
}

function drawMaze() {
	if (thinMaze) {
		drawMaze2();
		return;
	}
	var i, a;
	clearMaze();
	brush.beginPath();
	for (i = 0; i < maze.length; i++)
		for (a = 0; a < maze[i].length; a++)
			if (maze[i][a] == 1)
				rect(i, a);
	brush.fillStyle = "black";
	brush.fill();
	brush.closePath();

	brush.beginPath();
	for (i = 0; i < maze.length; i++)
		for (a = 0; a < maze[i].length; a++)
			if (maze[i][a] == 2)
				rect(i, a);
	brush.fillStyle = "blue";
	brush.fill();
	brush.closePath();

	if (visited) {
		brush.beginPath();
		for (i = 0; i < maze.length; i++)
			for (a = 0; a < maze[i].length; a++)
				if (correctPath[i][a])
					rect(i, a);
		brush.fillStyle = "yellow";
		brush.fill();
		brush.closePath();
	}

	if (showStartEnd)
		drawStartEnd();
}

function drawMaze2() {
	var i, a;
	clearMaze();

	if (visited) {
		brush.beginPath();
		for (i = 0; i < maze.length; i++)
			for (a = 0; a < maze[i].length; a++)
				if (correctPath[i][a])
					rect(i, a);
		brush.fillStyle = "yellow";
		brush.fill();
		brush.closePath();
	}

	if (showStartEnd)
		drawStartEnd2();

	brush.beginPath();
	for (i = 1; i < maze.length; i+=2)
		for (a = 0; a < maze[i].length; a++)
			if (maze[i][a] == 1) {
				brush.moveTo(i * blockWidth - blockWidth, a * blockWidth);
				brush.lineTo(i * blockWidth + blockWidth, a * blockWidth);
			}

	for (i = 0; i < maze.length; i++)
		for (a = 1; a < maze[i].length; a+=2)
			if (maze[i][a] == 1) {
				brush.moveTo(i * blockWidth, a * blockWidth - blockWidth);
				brush.lineTo(i * blockWidth, a * blockWidth + blockWidth);
			}
	brush.strokeStyle = "black";
	brush.stroke();
	brush.closePath();
}

function drawStartEnd() {
	brush.beginPath();
	rect(startMaze[0], startMaze[1]);
	brush.fillStyle = "green";
	brush.fill();
	brush.closePath();

	brush.beginPath();
	rect(endMaze[0], endMaze[1]);
	brush.fillStyle = "red";
	brush.fill();
	brush.closePath();
}

function drawStartEnd2() {
	brush.beginPath();
	brush.rect(startMaze[0] * blockWidth - blockWidth / 2, startMaze[1] * blockWidth - blockWidth / 2, blockWidth, blockWidth);
	brush.fillStyle = "green";
	brush.fill();
	brush.closePath();

	brush.beginPath();
	brush.rect(endMaze[0] * blockWidth - blockWidth / 2, endMaze[1] * blockWidth - blockWidth / 2, blockWidth, blockWidth);
	brush.fillStyle = "red";
	brush.fill();
	brush.closePath();
}

var animationX, animationY, anOn;

function startAnimation() {
	for (var i = 0; i < maze.length; i++)
		for (var a = 0; a < maze[i].length; a++)
			maze[i][a] = 1;
	animationX = startx;
	animationY = starty;
	anOn = 0;
	maze[animationX][animationY] = 2;
	drawMaze();
	animationInterval = setInterval(function() {
		if (animation[anOn] === undefined) {
			stopAnimation();
			drawMaze();
		} else {
			brush.beginPath();
			switch (animation[anOn]) {
				case 0:
					if (maze[animationX][animationY-2] == 1) {
						rect(animationX, animationY-1);
						rect(animationX, animationY-2);
						brush.fillStyle = "blue";
						brush.fill();
						maze[animationX][animationY-1] = 2;
						maze[animationX][animationY-2] = 2;
					} else {
						if (animation[anOn - 1] == 1) {
							maze[animationX][animationY] = 0;
							rect(animationX, animationY);
						}
						maze[animationX][animationY-1] = 0;
						rect(animationX, animationY-1);
						if (adjacent2(animationX, animationY-2, 1) === 0) {
							maze[animationX][animationY-2] = 0;
							rect(animationX, animationY-2);
						}
						brush.fillStyle = "white";
						brush.fill();
					}
					animationY -= 2;
					break;
				case 1:
					if (maze[animationX][animationY+2] == 1) {
						rect(animationX, animationY+1);
						rect(animationX, animationY+2);
						brush.fillStyle = "blue";
						brush.fill();
						maze[animationX][animationY+1] = 2;
						maze[animationX][animationY+2] = 2;
					} else {
						if (animation[anOn - 1] === 0) {
							maze[animationX][animationY] = 0;
							rect(animationX, animationY);
						}
						rect(animationX, animationY+1);
						maze[animationX][animationY+1] = 0;
						if (adjacent2(animationX, animationY+2, 1) === 0) {
							rect(animationX, animationY+2);
							maze[animationX][animationY+2] = 0;
						}
						brush.fillStyle = "white";
						brush.fill();
					}
					animationY += 2;
					break;
				case 2:
					if (maze[animationX+2][animationY] == 1) {
						rect(animationX+1, animationY);
						rect(animationX+2, animationY);
						brush.fillStyle = "blue";
						brush.fill();
						maze[animationX+1][animationY] = 2;
						maze[animationX+2][animationY] = 2;
					} else {
						if (animation[anOn - 1] == 3) {
							maze[animationX][animationY] = 0;
							rect(animationX, animationY);
						}
						rect(animationX + 1, animationY);
						maze[animationX+1][animationY] = 0;
						if (adjacent2(animationX+2, animationY, 1) === 0) {
							rect(animationX+2, animationY);
							maze[animationX+2][animationY] = 0;
						}
						brush.fillStyle = "white";
						brush.fill();
					}
					animationX += 2;
					break;
				case 3:
					if (maze[animationX-2][animationY] == 1) {
						rect(animationX-1, animationY);
						rect(animationX-2, animationY);
						brush.fillStyle = "blue";
						brush.fill();
						maze[animationX-1][animationY] = 2;
						maze[animationX-2][animationY] = 2;
					} else {
						if (animation[anOn - 1] == 2) {
							maze[animationX][animationY] = 0;
							rect(animationX, animationY);
						}
						rect(animationX-1, animationY);
						maze[animationX-1][animationY] = 0;
						if (adjacent2(animationX-2, animationY, 1) === 0) {
							maze[animationX-2][animationY] = 0;
							rect(animationX-2, animationY);
						}
						brush.fillStyle = "white";
						brush.fill();
					}
					animationX -= 2;
					break;
			}
			brush.closePath();
			anOn++;
			if (showStartEnd)
				if (thinMaze)
					drawStartEnd2();
				else drawStartEnd();
	//		 drawMaze();
		}
	}, totalAnimationTime * 900 / animation.length);
}

function stopAnimation() {
	clearInterval(animationInterval);
}

function rect(x, y) {
	if (thinMaze)
		brush.rect((x - 1) * blockWidth + 1, (y - 1) * blockWidth + 1, blockWidth * 2 - 2, blockWidth * 2 - 2);
	else brush.rect(x * blockWidth, y * blockWidth, blockWidth, blockWidth);
}

function newGame() {
	stopAnimation();
	getSettings();
	resizeMaze();

	maze = new Array(dimensions[0]);
	for (var i = 0; i < maze.length; i++) {
		maze[i] = new Array(dimensions[1]);
		for (var a = 0; a < maze[i].length; a++)
			maze[i][a] = 1;
	}
	visited = deadVisited = false;
	if (animate)
		animation = new Array(dimensions[0] * dimensions[1] / 2 | 0);
	animationOn = 0;
	startx = Math.random() * (dimensions[0] - 1) / 2 | 0 * 2 + 1;
	starty = Math.random() * (dimensions[1] - 1) / 2 | 0 * 2 + 1;
	maze[startx][starty] = 0;
	if (mazeStyle == "square" || mazeStyle == "awkward circle square")
		endMaze = [dimensions[0] / 2 - 0.5, dimensions[1] / 2 - 0.5];
	else endMaze = [dimensions[0] - 2, dimensions[1] - 2];
	startMaze = [1, 1];
	if (animate) {
		generateAnimationRecursive(startx, starty);
		startAnimation();
	} else {
		newGameRecursive(startx, starty);
		drawMaze();
	}
}

function getSettings() {
	dimensions = gameSettings.getOrSet('dimensions', [23, 23]);
	mazeStyle = gameSettings.getOrSet('mazeStyle', "normal");
	styleIntensity = gameSettings.getOrSet('styleIntensity', 8);
	showStartEnd = gameSettings.getOrSet('showStartEnd', true);
	thinMaze = gameSettings.getOrSet('thinMaze', false);
	animate = gameSettings.getOrSet('animate', false);
	totalAnimationTime = gameSettings.getOrSet('totalAnimationTime', 30);
	customResolution = gameSettings.getOrSet('customResolution', false);
	blockWidth = gameSettings.getOrSet('blockWidth', 8);
}

function newGameRecursive(x, y)	{
	var tempRandDs = getRandDs(x, y);
	for (var i = 0; i < tempRandDs.length; i++)
		switch (tempRandDs[i])	{
			case 0:
				if (y - 2 > 0 && maze[x][y-2] == 1)	{
					maze[x][y-1] = 0;
					maze[x][y-2] = 0;
					newGameRecursive(x, y-2);
				}
				break;
			case 1:
				if (y + 2 < maze[0].length && maze[x][y+2] == 1)	{
					maze[x][y+1] = 0;
					maze[x][y+2] = 0;
					newGameRecursive(x, y+2);
				}
				break;
			case 2:
				if (x + 2 < maze.length && maze[x+2][y] == 1)	{
					maze[x+1][y] = 0;
					maze[x+2][y] = 0;
					newGameRecursive(x+2, y);
				}
				break;
			case 3:
				if (x - 2 > 0 && maze[x-2][y] == 1)	{
					maze[x-1][y] = 0;
					maze[x-2][y] = 0;
					newGameRecursive(x-2, y);
				}
				break;
		}
}

function generateAnimationRecursive(x, y) {
	var tempRandDs = getRandDs(x, y);
	for (var i = 0; i < tempRandDs.length; i++)
		switch (tempRandDs[i])	{
			case 0:
				if (y - 2 > 0 && maze[x][y-2] == 1)	{
					maze[x][y-1] = 0;
					maze[x][y-2] = 0;
					animation[animationOn++] = 0;
					generateAnimationRecursive(x, y-2);
					animation[animationOn++] = 1;
				}
				break;
			case 1:
				if (y + 2 < maze[0].length && maze[x][y+2] == 1)	{
					maze[x][y+1] = 0;
					maze[x][y+2] = 0;
					animation[animationOn++] = 1;
					generateAnimationRecursive(x, y+2);
					animation[animationOn++] = 0;
				}
				break;
			case 2:
				if (x + 2 < maze.length && maze[x+2][y] == 1)	{
					maze[x+1][y] = 0;
					maze[x+2][y] = 0;
					animation[animationOn++] = 2;
					generateAnimationRecursive(x+2, y);
					animation[animationOn++] = 3;
				}
				break;
			case 3:
				if (x - 2 > 0 && maze[x-2][y] == 1)	{
					maze[x-1][y] = 0;
					maze[x-2][y] = 0;
					animation[animationOn++] = 3;
					generateAnimationRecursive(x-2, y);
					animation[animationOn++] = 2;
				}
				break;
		}
}

function adjacent(x, y, val) {
	var c = 0;
	if (x > 0 && maze[x-1][y] == val)
		c++;
	if (y > 0 && maze[x][y-1] == val)
		c++;
	if (x < maze.length - 1 && maze[x+1][y] == val)
		c++;
	if (y < maze[x].length - 1 && maze[x][y+1] == val)
		c++;
	return c;
}

function adjacent2(x, y, val) {
	var c = 0;
	if (x > 1 && maze[x-2][y] == val)
		c++;
	if (y > 1 && maze[x][y-2] == val)
		c++;
	if (x < maze.length - 2 && maze[x+2][y] == val)
		c++;
	if (y < maze[x].length - 2 && maze[x][y+2] == val)
		c++;
	return c;
}

function getRandDs(x, y) {
	var ds = [-1, -1, -1, -1];
	var ran;
	var relx = x * maze[0].length / maze.length;
	var rely = y * maze.length / maze[0].length;
	for (var i = 0; i < 3; i++) {
		do {
			switch (mazeStyle) {
				case "normal":
					ran = Math.random() * 4 | 0;
					break;
				case "vertical":
					if (Math.random() * 10 < styleIntensity)
						ran = Math.random() * 2 | 0;
					else ran = Math.random() * 4 | 0;
					break;
				case "horizontal":
					if (Math.random() * 10 < styleIntensity)
						ran = Math.random() * 2 | 0 + 2;
					else ran = Math.random() * 4 | 0;
					break;
				case "checkerboard":
					if (((x / dimensions[0] * 5 | 0) % 2 === 0) == ((y / dimensions[1] * 5 | 0) % 2 === 0))
						if (Math.random() * 10 < styleIntensity)
							ran = Math.random() * 2 | 0;
						else ran = Math.random() * 4 | 0;
					else if (Math.random() * 10 < styleIntensity)
						ran = Math.random() * 2 | 0 + 2;
					else ran = Math.random() * 4 | 0;
					break;
				case "inward x":
					if (relx < y === x > maze.length - rely)
						if (Math.random() * 10 < styleIntensity)
							ran = Math.random() * 2 | 0;
						else ran = Math.random() * 4 | 0;
					else if (Math.random() * 10 < styleIntensity)
						ran = Math.random() * 2 | 0 + 2;
					else ran = Math.random() * 4 | 0;
					break;
				case "square":
					if (relx < y !== x > maze.length - rely)
						if (Math.random() * 10 < styleIntensity)
							ran = Math.random() * 2 | 0;
						else ran = Math.random() * 4 | 0;
					else if (Math.random() * 10 < styleIntensity)
						ran = Math.random() * 2 | 0 + 2;
					else ran = Math.random() * 4 | 0;
					break;
				// TODO: Make circles into ovals on uneven x y
				case "awkward circle":
					if (awkwardCircleVertical(x, y))
						if (Math.random() * 10 < styleIntensity)
							ran = Math.random() * 2 | 0;
						else ran = Math.random() * 4 | 0;
					else if (Math.random() * 10 < styleIntensity)
						ran = Math.random() * 2 | 0 + 2;
					else ran = Math.random() * 4 | 0;
					break;
				case "awkward circle square":
					if (!awkwardCircleVertical(x, y))
						if (Math.random() * 10 < styleIntensity)
							ran = Math.random() * 2 | 0;
						else ran = Math.random() * 4 | 0;
					else if (Math.random() * 10 < styleIntensity)
						ran = Math.random() * 2 | 0 + 2;
					else ran = Math.random() * 4 | 0;
					break;
			}
		} while (ds[ran] != -1);
		ds[ran] = i;
	}
	for (var a = 0; a < 4; a++)
		if (ds[a] == -1) {
			ds[a] = 3;
			break;
		}
	return ds;
}

function awkwardCircleVertical(x, y) {
	var deltaX = Math.abs(x - maze.length / 2);
	var deltaY = Math.abs(y - maze[x].length / 2);
	var relx = x * maze[y].length / maze.length;
	var rely = y * maze.length / maze[y].length;
	if (Math.pow(deltaX, 2) / Math.pow(dimensions[0] / 2 - 0.5, 2) + Math.pow(deltaY, 2) / Math.pow(dimensions[1] / 2 - 0.5, 2) <= 1)
		return relx < y === x > maze.length - rely;
	return relx < y !== x > maze.length - rely;
}

function solveMaze() {
	var already = visited;
	visited = new Array(maze.length);
	correctPath = new Array(maze.length);
	for (var i = 0; i < maze.length; i++) {
		visited[i] = new Array(maze[i].length);
		correctPath[i] = new Array(maze[i].length);
		for (var a = 0; a < maze[i].length; a++) {
			visited[i][a] = false;
			correctPath[i][a] = false;
		}
	}
	if (!already)
		solveMazeRecursive(startMaze[0], startMaze[1]);
	else visited = false;
	drawMaze();
}

function solveMazeRecursive(row, col)	{
	if (row == endMaze[0] && col == endMaze[1]) {
		correctPath[row][col] = true;
		return true;
	}
	visited[row][col] = true;
	if (maze[row-1][col] === 0 && !visited[row-1][col])
		if (solveMazeRecursive(row - 1, col)) {
			correctPath[row][col] = true;
			return true;
		}
	if (maze[row+1][col] === 0 && !visited[row+1][col])
		if (solveMazeRecursive(row + 1, col)) {
			correctPath[row][col] = true;
			return true;
		}
	if (maze[row][col-1] === 0 && !visited[row][col-1])
		if (solveMazeRecursive(row, col - 1)) {
			correctPath[row][col] = true;
			return	true;
		}
	if (maze[row][col+1] === 0 && !visited[row][col+1])
		if (solveMazeRecursive(row, col + 1)) {
			correctPath[row][col] = true;
			return true;
		}
	return false;
}

function removeDeadEnds(threshold) {
	deadVisited = new Array(maze.length);
	for (var i = 0; i < maze.length; i++) {
		deadVisited[i] = new Array(maze[i].length);
		for (var a = 0; a < maze[i].length; a++)
			deadVisited[i][a] = false;
	}
	removeDeadEndsRecursive(startMaze[0], startMaze[1], threshold);
	drawMaze();
}

function removeDeadEndsRecursive(row, col, chance)	{
	deadVisited[row][col] = true;
	if (adjacent(row, col, 1) === 3) {
		if (Math.random() < chance)
			switch (Math.random() * 4 | 0) {
				case 0:
					if (maze[row-1][col] === 1 && row !== 1) {
						maze[row-1][col] = 0;
						break;
					}
				case 1:
					if (maze[row+1][col] === 1 && row !== maze.length - 2) {
						maze[row+1][col] = 0;
						break;
					}
				case 2:
					if (maze[row][col-1] === 1 && col !== 1) {
						maze[row][col-1] = 0;
						break;
					}
				case 3:
					if (maze[row][col+1] === 1 && col !== maze[row].length - 2) {
						maze[row][col+1] = 0;
						break;
					}
				default:
					if (maze[row-1][col] === 1 && row !== 1)
						maze[row-1][col] = 0;
					else if (maze[row+1][col] === 1 && row !== maze.length - 2)
						maze[row+1][col] = 0;
					else if (maze[row][col-1] === 1 && col !== 1)
						maze[row][col-1] = 0;
					else console.error("BOZO ALERT");

			}
	}
	if (maze[row-1][col] === 0 && !deadVisited[row-1][col])
		removeDeadEndsRecursive(row - 1, col, chance);
	if (maze[row+1][col] === 0 && !deadVisited[row+1][col])
		removeDeadEndsRecursive(row + 1, col, chance);
	if (maze[row][col-1] === 0 && !deadVisited[row][col-1])
		removeDeadEndsRecursive(row, col - 1, chance);
	if (maze[row][col+1] === 0 && !deadVisited[row][col+1])
		removeDeadEndsRecursive(row, col + 1, chance);
}

function shuffle(array) {
	var currentIndex = array.length, temporaryValue, randomIndex;

	// While there remain elements to shuffle...
	while (0 !== currentIndex) {

		// Pick a remaining element...
		randomIndex = Math.floor(Math.random() * currentIndex);
		currentIndex -= 1;

		// And swap it with the current element.
		temporaryValue = array[currentIndex];
		array[currentIndex] = array[randomIndex];
		array[randomIndex] = temporaryValue;
	}

	return array;
}

function getNewSettings() {
	return {
		'dimensions': [getInputValue('width'), getInputValue('height')],
		'mazeStyle': getInputValue('mazeStyle'),
		'styleIntensity': getInputValue('styleIntensity'),
		'showStartEnd': getInputValue('showStart'),
		'thinMaze': getInputValue('thinMaze'),
		'animate': getInputValue('animate'),
		'totalAnimationTime': getInputValue('duration'),
		'customResolution': getInputValue('customResolution'),
		'blockWidth': getInputValue('blockWidth'),
	}
}

function populateSettingsForm(settings) {
	setInputValue('width', dimensions[0]);
	setInputValue('height', dimensions[1]);
	setInputValue('mazeStyle', mazeStyle);
	setInputValue('styleIntensity', styleIntensity);
	setInputValue('showStart', showStartEnd);
	setInputValue('thinMaze', thinMaze);
	setInputValue('animate', animate);
	setInputValue('duration', totalAnimationTime);
	setInputValue('customResolution', customResolution);
	setInputValue('blockWidth', blockWidth);
}

document.addEventListener('keypress', function (event) {
	var tempDirection;
	switch (event.which) {
		case 115: case 83: // s
			showSettingsForm();
			break;
		case 110: case 78: // n
			newGame();
			return;
	}
});
