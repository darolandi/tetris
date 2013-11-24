@echo off

cd dist
mkdir music
cd music
copy ..\..\music\* .

cd ..
mkdir images
cd images
copy ..\..\images\* .

cd ..
cd lib
mkdir native
cd native
copy ..\..\..\lib\native\* .

cd ..\..
del README.txt
7za a -tzip Tetris *
move Tetris.zip ..

echo "Make success!"
pause