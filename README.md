# FoodFindAR

FoodFindAR is an AR-App, that shows the distance and direction to a nearby restaurant of your choice.

## Usage
For usage instructions, please refer to the wiki 

[(Wiki-Link)](https://github.com/marleo/AppDev_FoodFindAR/wiki/Usage-Instructions)

## Features

- Get directions to your chosen restaurant via an AR-View

- Check restaurant ratings

- See which restaurants are nearby

### Libraries / API's used

OverpassAPI (https://github.com/drolbr/Overpass-API) - fetching location data

GSON (https://github.com/google/gson) - parsing JSON Objects

Volley (https://github.com/google/volley) - Handling HTTP Requests

_for further information concerning the used API's, refer to the Github Pages wiki:_ https://github.com/marleo/AppDev_FoodFindAR/wiki

## Prototype

Check it out (https://www.figma.com/proto/kHPQnljHRPmvtl42gGO1uq/Food-FindAR?page-id=0%3A1&node-id=2%3A84&viewport=1254%2C1174%2C0.2884855270385742&scaling=contain)

## Known Bugs

The AR-View is only working reliably if you open Google Maps and check your location there beforehand. Otherwise the Location received by the FusedLocationAdapter might get old, non accurate location data. To prevent this, open Google Maps, press the locate button on the bottom right, then exit Maps and open FoodFindAR. This has to be replicated everytime, you want to get the accurate AR position.

## Groupmembers

Mira Kofler

Christian Raunjak

Mario Leopold
