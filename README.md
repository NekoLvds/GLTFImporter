# GLTFImporter
This is a java importer for the gltf format into the JavaFX Framework.  
The gltf format is a 3d format for storing assets or entire scenes
and transfer them between applications.

## Motivation
The Motivation comes from having a great 3d framework with JavaFX but
not really a way to import our models we make in Blender or any other 
software into JavaFX.  
Although there are many projects on the internet many are abandoned 
by there creators and are two+ years since there last update ignoring
that many were never finished.

## Goal
The goal is to have an easy to use all round solution for importing
gltf files into JavaFX.  
  
These divides into following functionalities:
- Import of the model itself
- Easy way to access its properties like animations, or 
  translations and change/start these
- Different ways of importing (local file / http interface ...)

## Current state
Currently, the importer only reads the json and binary data from 
the gltf file and stores it.  
The nest step is to port that data into the JavaFX Framework.
