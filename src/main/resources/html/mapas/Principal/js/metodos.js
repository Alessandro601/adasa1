

function setMarkerPosition(lat, lng) {
            var clickLatLng = new google.maps.LatLng(lat, lng);
            marker.setPosition(clickLatLng);
        }
function startJumping(){
    marker.setAnimation(google.maps.Animation.BOUNCE);
}

function stopJumping(){
    marker.setAnimation(null);
}

function setMapCenter(lat, lng) {
    var latlng = new google.maps.LatLng(lat, lng);
    map.setCenter(latlng);
}

function switchSatellite() {
    var mapOptions = {
        mapTypeId: google.maps.MapTypeId.SATELLITE
       
    };
    map.setOptions(mapOptions);
    // mudar o estilo do mapa para default
    map.setOptions({styles: styles[listStyles[0]]});
    
}

function switchRoadmap() {
    var mapOptions = {
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    map.setOptions(mapOptions);
    // mudar o estilo do mapa para default
    map.setOptions({styles: styles[listStyles[0]]});
   
}

function switchHybrid() {
    var mapOptions = {
        mapTypeId: google.maps.MapTypeId.HYBRID
    };
    map.setOptions(mapOptions);
    // mudar o estilo do mapa para default
    map.setOptions({styles: styles[listStyles[0]]});
}

function switchTerrain() {
    var mapOptions = {
        mapTypeId: google.maps.MapTypeId.TERRAIN
    };
    map.setOptions(mapOptions);
    map.setOptions({styles: styles.null});

}

function setDarkMarkerIcon() {
    marker.setIcon("img/Pin.png");
}

function setLightMarkerIcon() {
    marker.setIcon("img/Pin_s.png");
}

//-- funcao para as shapes --//
function openShape(i) {

	if(layers[i].getMap()==null) {
 		layers[i].setMap(map);
	}
	else {
 			layers[i].setMap(null);
	}
	
 }

function getCoordClick(event){
	
	var location = event.latLng;
	var lat = location.lat();
	var lng = location.lng();
	
	setMarkerPosition(lat, lng);
	
	obterUTMDMSMapClick (lat,lng);

}

function setMarkerPosition (lat, lng) {

	var clickLatLng = new google.maps.LatLng(lat, lng);

	// limpar marcadores antigos dos cliques //
	marker.setMap(null);

	
	// limpar marcadores antigos das buscas por endereços //
	markers.forEach(function(marker) {
	  marker.setMap(null);
	});
	markers = [];


	// criar um novo marcador a partir da posição clicada //
	marker = new google.maps.Marker({
	    position: clickLatLng,
	    map: map,
	    title: 'Coordenada clicada'
	  });

	console.log(lat + ',' + lng);

}
function setZoom (i) {
	map.setZoom(i);
	map.setCenter(marker.getPosition());
}


function obterUTMDMS (typeCoordinate, lat,lon) {
	// conversão DD para DMS //
 	var lat = Dms.parseDMS(lat);
 	var lon = Dms.parseDMS(lon);
 	var latLon = new LatLon(lat, lon); // 51.4778°N, 000.0015°W
 	
 	// conversão DD para UTM //
 	var toUTM = latLon.toUtm();
 	
 	var resultadoLatLon = latLon + ' e ' + toUTM;
	
 	app.handle (typeCoordinate, lat + "," + lon, latLon, toUTM);
	//console.log(latLon + ' e ' + toUTM);
 	setMarkerPosition(lat, lon);
 	setMapCenter(lat, lon);
	
}

function obterDDUTM (typeCoordinate, latDMS, lonDMS) {
	
	// conversão da coordenada convertida DMS em DD //
	var latDMSToDD = Dms.parseDMS(latDMS); // Dms.toLat (utmToDMSaa.lat, 'dms', 0);
	var lonDMSToDD = Dms.parseDMS(lonDMS); //Dms.toLon (utmToDMSaa.lon, 'dms', 0);
	
	
	// conversão DD para UTM //
 	var latLon2UTM = new LatLon (latDMSToDD, lonDMSToDD);
	var toUTM = latLon2UTM.toUtm();
	
	if (latDMSToDD.toString().length > 11) {
		
		latDMSToDD = latDMSToDD.toString().substring (0,11)
 		}
	
	if (lonDMSToDD.toString().length > 11) {
		lonDMSToDD = lonDMSToDD.toString().substring (0,11);
		}
	 
		
	app.handle (typeCoordinate, latDMSToDD + "," + lonDMSToDD, latDMS + "," + lonDMS, toUTM);
	
	setMarkerPosition(latDMSToDD, lonDMSToDD);
	setMapCenter(latDMSToDD, lonDMSToDD);
}

function obterDDDMS (typeCoordinate, utm) {
	
	// reversão da coordenada convertida UTM para DMS
		var UTMTODMS = Utm.parse(utm);
		var utmToDMS = UTMTODMS.toLatLonE();

	// reversão da coordenada convertida DMS em DD //
	var latDMSToDD = Dms.parseDMS(utmToDMS.lat); // Dms.toLat (utmToDMSaa.lat, 'dms', 0);
	var lonDMSToDD = Dms.parseDMS(utmToDMS.lon); //Dms.toLon (utmToDMSaa.lon, 'dms', 0);
	
	if (latDMSToDD.toString().length > 11) {
		
		latDMSToDD = latDMSToDD.toString().substring (0,11)
 		}
	
	if (lonDMSToDD.toString().length > 11) {
		lonDMSToDD = lonDMSToDD.toString().substring (0,11);
		}
	
	app.handle (typeCoordinate, latDMSToDD + "," + lonDMSToDD, utmToDMS, utm);
	
	setMarkerPosition(latDMSToDD, lonDMSToDD);
	setMapCenter(latDMSToDD, lonDMSToDD);
 	
}

function obterUTMDMSMapClick (lat,lon) {
	// conversão DD para DMS //
 	var lat = Dms.parseDMS(lat);
 	var lon = Dms.parseDMS(lon);
 	var latLon = new LatLon(lat, lon); // 51.4778°N, 000.0015°W
 	
 	// conversão DD para UTM //
 	var toUTM = latLon.toUtm();
 	
 	var resultadoLatLon = latLon + ' e ' + toUTM;
 	
 	
 	if (lat.toString().length > 11) {
		
 		lat = lat.toString().substring (0,11)
 		}
	
	if (lon.toString().length > 11) {
		lon = lon.toString().substring (0,11);
		}
	
 	app.setAllCoords(lat + ',' + lon, latLon, toUTM);
 	app.setCoords(lat, lon);
 	
}

function setZoomIn () {
	
	var zoom = map.getZoom() + 2;
	
	map.setZoom(zoom);
	map.setCenter(marker.getPosition());

}

function setZoomOut () {
	
	var zoom = map.getZoom() - 2;
	
	map.setZoom(zoom);
	map.setCenter(marker.getPosition());

}

function setEnderecoInterferencias (strEndereco, strInterferencia,strDetalhes) {
	// array de marcadores
	var markers = [];

	/*
	separar strDetalhes por interferencias
	0 = "" 
	1 - "Subterrânea,Rio Corumbá,1,Arquivado" 
	2 - "Subterrânea,Rio Descoberto,14,Em Análise"
	*/
	
	var arrayDetalhes = strDetalhes.split('|');
		/*
		separar dados das das interferencias 
			0 - tipo inter 1 - Bacia 2 - UH  3 - Situação
		*/  
		  var d = [];

		      for (i=0;i<arrayDetalhes.length;i++){
		        d[i] = arrayDetalhes[i].split(',');
		        // fica assim d[1][0] =  subterrânea
		      }

	// separar por | a string recebida
	var arrayInterferencias = strInterferencia.split('|');
		// array para receber esta separacao
		var latlon = [];
		  // separar por , e adicionar na array latlon
		  for (i=0;i<arrayInterferencias.length;i++){
		    latlon [i] = arrayInterferencias[i].split(',');
		
		    }
 
	// separar a array latlon em nova array  
	var interferencia = [];
		// separar endereco
		var e = strEndereco.split(',');
			  // transformar em formato maps.LatLng
			  var endereco = new google.maps.LatLng({
			        lat: parseFloat(e[0]), 
			        lng: parseFloat(e[1])});
    
			  var imgInterferencia = 'http://icons.iconarchive.com/icons/double-j-design/super-mono-3d/32/button-info-icon.png';
			  var imgEndereco = 'http://icons.iconarchive.com/icons/double-j-design/super-mono-3d/32/home-icon.png';

	// implantar pontos e linhas
	for (i=1;i<latlon.length;i++){

		var infowindow = new google.maps.InfoWindow();
		infowindow.setContent('<b>Interferência: ' + d[i][0] + '</b>'
								+'<div>Bacia: ' + d[i][1] + '</div>'
								+'<div>Unidade Hidrográfica: ' + d[i][2] + '</div>'
								+'<div>Situação: ' + d[i][3] + ' </div>');

	interferencia [i] = new google.maps.LatLng({
	    lat: parseFloat(latlon[i][0]), 
	    lng: parseFloat(latlon[i][1])});

	// preparar as coordenadas para mostrar endereco e interferecia
	var list = [endereco,interferencia[i]];
	
				// criar linha
		  var linhas = new google.maps.Polyline({
		  path: list,
		  geodesic: true,
		  strokeColor: '#62CBEC',
		  strokeOpacity: 1.0,
		  strokeWeight: 2
		  });
	  
			// mostrar linha
			linhas.setMap(map);

	marker = new google.maps.Marker({
	    position: interferencia[i],
	  icon: imgInterferencia,
	    map: map,
	    title: interferencia[i].toString()
	  });

  
	//creates an infowindow 'key' in the marker.
	marker.infowindow = infowindow;

	//finally call the explicit infowindow object
	marker.addListener('click', function() {
	return this.infowindow.open(map, this);
	})

	// Alternate way of adding infowindow listeners
	google.maps.event.addListener(marker, 'click', function() {
		this.infowindow.open(map, this); 
	});


	} // fim loop for

	// marcador do endereco
	marker [0] = new google.maps.Marker({
	position: endereco,
	icon: imgEndereco,
	map: map,
	title: 'Hello World!'
	});

} // fim funcao setEnderecoInterferencias


function mostrarDemandas (strEndereco, strInfoDemandas) {
	
	
	var e = strEndereco.split(',');
		  
	// transformar em formato maps.LatLng
	var endereco = new google.maps.LatLng({
		        lat: parseFloat(e[0]), 
		        lng: parseFloat(e[1])});
		 
	/* separar documentos relacionados */
	var arrayDocumentos = strInfoDemandas.split('|');
	
	
	  var imgInterferencia = 'http://icons.iconarchive.com/icons/double-j-design/super-mono-3d/32/button-info-icon.png';
	  var imgEndereco = 'http://icons.iconarchive.com/icons/double-j-design/super-mono-3d/32/home-icon.png';
	
	  var arrayInformacoesDocumentos = [];
	
	  var x =
		  
		  '<tr><td>' 
	      +  	'</td><td>'
	      + 	'</td><td>'
	      + 	'</td></tr>'
	          
	  ;
	
	  for (i=1;i<arrayDocumentos.length;i++){

		arrayInformacoesDocumentos[i] = arrayDocumentos[i].split(',');
		        // fica assim d[1][0] =  subterrânea
	
		 var y =
			 
			 '<tr><td>'  
		      +  	arrayInformacoesDocumentos[i][0] + '</td><td>'
		      + 	arrayInformacoesDocumentos[i][1] + '</td><td>'
		      + 	arrayInformacoesDocumentos[i][2] + '</td></tr>'
		          
		;

		 x += y;
	
	  }
	
	// variavel x adicionar as inforações envolvidas com a tag <tr> e <td>
	var table = 
			
			"<table>"
		+	"<tr>"
		+	"<th>Documento</th>"
		+	"<th>Sei</th>"
		+	"<th>Processo</th>"
		+	"</tr>"
		+ 	x 	+ ""
		+	"</table>";
	  
	 // criar marcador 
	var marker = new google.maps.Marker({
	    position: endereco,
	    map: map,
	    icon: imgEndereco,
	    title: 'Endereço'
	});
	
	// criar infoWindow
	var infowindow = new google.maps.InfoWindow({
			 content: table,
			 
	});
   
	// abrir automaticamente infoWidows
	infowindow.open(map, marker);
	
	// ouvinte para o click e abrir o infoWindows
	marker.addListener('click', function() {
		    infowindow.open(map, marker);
	});

} // fim funcao mostrar demandas



//variavel com lista dos estilos do mapa
var listStyles = ["default","night", "darkBlue", "green", "retro"];

// mudar o estilo do mapa com as opcoes defaul, night etc
function mudarEstiloMapa (i) {
	
	var mapOptions = {
	        mapTypeId: google.maps.MapTypeId.ROADMAP
	    };
	    map.setOptions(mapOptions);
	    
	map.setOptions({styles: styles[listStyles[i]]});
}


var styles = {
		
	default: null,
	
	night: [
	  {elementType: 'geometry', stylers: [{color: '#242f3e'}]},
	  {elementType: 'labels.text.stroke', stylers: [{color: '#242f3e'}]},
	  {elementType: 'labels.text.fill', stylers: [{color: '#746855'}]},
	  {
	    featureType: 'administrative.locality',
	    elementType: 'labels.text.fill',
	    stylers: [{color: '#d59563'}]
	  },
	  {
	    featureType: 'poi',
	    elementType: 'labels.text.fill',
	    stylers: [{color: '#d59563'}]
	  },
	  {
	    featureType: 'poi.park',
	    elementType: 'geometry',
	    stylers: [{color: '#263c3f'}]
	  },
	  {
	    featureType: 'poi.park',
	    elementType: 'labels.text.fill',
	    stylers: [{color: '#6b9a76'}]
	  },
	  {
	    featureType: 'road',
	    elementType: 'geometry',
	    stylers: [{color: '#38414e'}]
	  },
	  {
	    featureType: 'road',
	    elementType: 'geometry.stroke',
	    stylers: [{color: '#212a37'}]
	  },
	  {
	    featureType: 'road',
	    elementType: 'labels.text.fill',
	    stylers: [{color: '#9ca5b3'}]
	  },
	  {
	    featureType: 'road.highway',
	    elementType: 'geometry',
	    stylers: [{color: '#746855'}]
	  },
	  {
	    featureType: 'road.highway',
	    elementType: 'geometry.stroke',
	    stylers: [{color: '#1f2835'}]
	  },
	  {
	    featureType: 'road.highway',
	    elementType: 'labels.text.fill',
	    stylers: [{color: '#f3d19c'}]
	  },
	  {
	    featureType: 'transit',
	    elementType: 'geometry',
	    stylers: [{color: '#2f3948'}]
	  },
	  {
	    featureType: 'transit.station',
	    elementType: 'labels.text.fill',
	    stylers: [{color: '#d59563'}]
	  },
	  {
	    featureType: 'water',
	    elementType: 'geometry',
	    stylers: [{color: '#17263c'}]
	  },
	  {
	    featureType: 'water',
	    elementType: 'labels.text.fill',
	    stylers: [{color: '#515c6d'}]
	  },
	  {
	    featureType: 'water',
	    elementType: 'labels.text.stroke',
	    stylers: [{color: '#17263c'}]
	  }
	],
	
	retro: [
	  {elementType: 'geometry', stylers: [{color: '#ebe3cd'}]},
	  {elementType: 'labels.text.fill', stylers: [{color: '#523735'}]},
	  {elementType: 'labels.text.stroke', stylers: [{color: '#f5f1e6'}]},
	  {
	    featureType: 'administrative',
	    elementType: 'geometry.stroke',
	    stylers: [{color: '#c9b2a6'}]
	  },
	  {
	    featureType: 'administrative.land_parcel',
	    elementType: 'geometry.stroke',
	    stylers: [{color: '#dcd2be'}]
	  },
	  {
	    featureType: 'administrative.land_parcel',
	    elementType: 'labels.text.fill',
	    stylers: [{color: '#ae9e90'}]
	  },
	  {
	    featureType: 'landscape.natural',
	    elementType: 'geometry',
	    stylers: [{color: '#dfd2ae'}]
	  },
	  {
	    featureType: 'poi',
	    elementType: 'geometry',
	    stylers: [{color: '#dfd2ae'}]
	  },
	  {
	    featureType: 'poi',
	    elementType: 'labels.text.fill',
	    stylers: [{color: '#93817c'}]
	  },
	  {
	    featureType: 'poi.park',
	    elementType: 'geometry.fill',
	    stylers: [{color: '#a5b076'}]
	  },
	  {
	    featureType: 'poi.park',
	    elementType: 'labels.text.fill',
	    stylers: [{color: '#447530'}]
	  },
	  {
	    featureType: 'road',
	    elementType: 'geometry',
	    stylers: [{color: '#f5f1e6'}]
	  },
	  {
	    featureType: 'road.arterial',
	    elementType: 'geometry',
	    stylers: [{color: '#fdfcf8'}]
	  },
	  {
	    featureType: 'road.highway',
	    elementType: 'geometry',
	    stylers: [{color: '#f8c967'}]
	  },
	  {
	    featureType: 'road.highway',
	    elementType: 'geometry.stroke',
	    stylers: [{color: '#e9bc62'}]
	  },
	  {
	    featureType: 'road.highway.controlled_access',
	    elementType: 'geometry',
	    stylers: [{color: '#e98d58'}]
	  },
	  {
	    featureType: 'road.highway.controlled_access',
	    elementType: 'geometry.stroke',
	    stylers: [{color: '#db8555'}]
	  },
	  {
	    featureType: 'road.local',
	    elementType: 'labels.text.fill',
	    stylers: [{color: '#806b63'}]
	  },
	  {
	    featureType: 'transit.line',
	    elementType: 'geometry',
	    stylers: [{color: '#dfd2ae'}]
	  },
	  {
	    featureType: 'transit.line',
	    elementType: 'labels.text.fill',
	    stylers: [{color: '#8f7d77'}]
	  },
	  {
	    featureType: 'transit.line',
	    elementType: 'labels.text.stroke',
	    stylers: [{color: '#ebe3cd'}]
	  },
	  {
	    featureType: 'transit.station',
	    elementType: 'geometry',
	    stylers: [{color: '#dfd2ae'}]
	  },
	  {
	    featureType: 'water',
	    elementType: 'geometry.fill',
	    stylers: [{color: '#b9d3c2'}]
	  },
	  {
	    featureType: 'water',
	    elementType: 'labels.text.fill',
	    stylers: [{color: '#92998d'}]
	  }
	],
	
	darkBlue: [
	  {
	      "featureType": "all",
	      "elementType": "labels.text.fill",
	      "stylers": [
	          {
	              "saturation": 100
	          },
	          {
	              "color": "#7b94be"
	          },
	          {
	              "lightness": 50
	          }
	      ]
	  },
	  {
	      "featureType": "all",
	      "elementType": "labels.text.stroke",
	      "stylers": [
	          {
	              "visibility": "off"
	          }
	      ]
	  },
	  {
	      "featureType": "all",
	      "elementType": "labels.icon",
	      "stylers": [
	          {
	              "visibility": "off"
	          }
	      ]
	  },
	  {
	      "featureType": "administrative",
	      "elementType": "all",
	      "stylers": [
	          {
	              "visibility": "simplified"
	          },
	          {
	              "lightness": "26"
	          },
	          {
	              "gamma": "2"
	          }
	      ]
	  },
	  {
	      "featureType": "administrative",
	      "elementType": "geometry.fill",
	      "stylers": [
	          {
	              "color": "#000045"
	          },
	          {
	              "lightness": 20
	          }
	      ]
	  },
	  {
	      "featureType": "administrative",
	      "elementType": "geometry.stroke",
	      "stylers": [
	          {
	              "lightness": 17
	          },
	          {
	              "weight": 1.2
	          }
	      ]
	  },
	  {
	      "featureType": "landscape",
	      "elementType": "geometry",
	      "stylers": [
	          {
	              "color": "#2b3467"
	          },
	          {
	              "visibility": "on"
	          },
	          {
	              "saturation": "0"
	          }
	      ]
	  },
	  {
	      "featureType": "landscape",
	      "elementType": "geometry.stroke",
	      "stylers": [
	          {
	              "visibility": "off"
	          }
	      ]
	  },
	  {
	      "featureType": "landscape",
	      "elementType": "labels.text.stroke",
	      "stylers": [
	          {
	              "visibility": "off"
	          }
	      ]
	  },
	  {
	      "featureType": "poi",
	      "elementType": "all",
	      "stylers": [
	          {
	              "visibility": "off"
	          }
	      ]
	  },
	  {
	      "featureType": "poi",
	      "elementType": "geometry",
	      "stylers": [
	          {
	              "color": "#5d3ba2"
	          },
	          {
	              "lightness": 21
	          },
	          {
	              "visibility": "off"
	          }
	      ]
	  },
	  {
	      "featureType": "poi",
	      "elementType": "geometry.fill",
	      "stylers": [
	          {
	              "visibility": "simplified"
	          }
	      ]
	  },
	  {
	      "featureType": "poi",
	      "elementType": "geometry.stroke",
	      "stylers": [
	          {
	              "visibility": "off"
	          }
	      ]
	  },
	  {
	      "featureType": "poi",
	      "elementType": "labels",
	      "stylers": [
	          {
	              "visibility": "off"
	          }
	      ]
	  },
	  {
	      "featureType": "poi",
	      "elementType": "labels.text",
	      "stylers": [
	          {
	              "visibility": "off"
	          }
	      ]
	  },
	  {
	      "featureType": "poi",
	      "elementType": "labels.icon",
	      "stylers": [
	          {
	              "visibility": "off"
	          }
	      ]
	  },
	  {
	      "featureType": "road.highway",
	      "elementType": "geometry.fill",
	      "stylers": [
	          {
	              "color": "#4a5478"
	          },
	          {
	              "lightness": "14"
	          }
	      ]
	  },
	  {
	      "featureType": "road.highway",
	      "elementType": "geometry.stroke",
	      "stylers": [
	          {
	              "visibility": "off"
	          }
	      ]
	  },
	  {
	      "featureType": "road.highway",
	      "elementType": "labels.text.fill",
	      "stylers": [
	          {
	              "color": "#ffffff"
	          }
	      ]
	  },
	  {
	      "featureType": "road.highway.controlled_access",
	      "elementType": "geometry.stroke",
	      "stylers": [
	          {
	              "lightness": 20
	          }
	      ]
	  },
	  {
	      "featureType": "road.arterial",
	      "elementType": "geometry",
	      "stylers": [
	          {
	              "lightness": "25"
	          }
	      ]
	  },
	  {
	      "featureType": "road.arterial",
	      "elementType": "geometry.fill",
	      "stylers": [
	          {
	              "color": "#4a5478"
	          },
	          {
	              "saturation": "9"
	          },
	          {
	              "lightness": "1"
	          }
	      ]
	  },
	  {
	      "featureType": "road.arterial",
	      "elementType": "geometry.stroke",
	      "stylers": [
	          {
	              "visibility": "off"
	          }
	      ]
	  },
	  {
	      "featureType": "road.arterial",
	      "elementType": "labels.text.fill",
	      "stylers": [
	          {
	              "color": "#ffffff"
	          }
	      ]
	  },
	  {
	      "featureType": "road.local",
	      "elementType": "geometry",
	      "stylers": [
	          {
	              "color": "#404b6d"
	          },
	          {
	              "lightness": "9"
	          },
	          {
	              "saturation": "8"
	          }
	      ]
	  },
	  {
	      "featureType": "road.local",
	      "elementType": "geometry.stroke",
	      "stylers": [
	          {
	              "visibility": "off"
	          }
	      ]
	  },
	  {
	      "featureType": "road.local",
	      "elementType": "labels.text.fill",
	      "stylers": [
	          {
	              "color": "#f3f3f3"
	          }
	      ]
	  },
	  {
	      "featureType": "transit",
	      "elementType": "all",
	      "stylers": [
	          {
	              "visibility": "off"
	          }
	      ]
	  },
	  {
	      "featureType": "transit",
	      "elementType": "geometry",
	      "stylers": [
	          {
	              "color": "#000045"
	          },
	          {
	              "lightness": 19
	          },
	          {
	              "visibility": "on"
	          }
	      ]
	  },
	  {
	      "featureType": "transit.line",
	      "elementType": "all",
	      "stylers": [
	          {
	              "visibility": "off"
	          }
	      ]
	  },
	  {
	      "featureType": "transit.line",
	      "elementType": "geometry",
	      "stylers": [
	          {
	              "visibility": "on"
	          },
	          {
	              "color": "#ffffff"
	          }
	      ]
	  },
	  {
	      "featureType": "transit.line",
	      "elementType": "geometry.fill",
	      "stylers": [
	          {
	              "color": "#b7daff"
	          },
	          {
	              "visibility": "simplified"
	          }
	      ]
	  },
	  {
	      "featureType": "transit.line",
	      "elementType": "geometry.stroke",
	      "stylers": [
	          {
	              "visibility": "off"
	          }
	      ]
	  },
	  {
	      "featureType": "transit.station",
	      "elementType": "geometry",
	      "stylers": [
	          {
	              "color": "#4ca1fa"
	          },
	          {
	              "visibility": "off"
	          }
	      ]
	  },
	  {
	      "featureType": "water",
	      "elementType": "geometry",
	      "stylers": [
	          {
	              "lightness": 17
	          }
	      ]
	  },
	  {
	      "featureType": "water",
	      "elementType": "geometry.fill",
	      "stylers": [
	          {
	              "color": "#30427a"
	          }
	      ]
	  }
	],
	green:
	
	[
	  {
	      "featureType": "water",
	      "elementType": "geometry",
	      "stylers": [
	          {
	              "hue": "#165c64"
	          },
	          {
	              "saturation": 34
	          },
	          {
	              "lightness": -69
	          },
	          {
	              "visibility": "on"
	          }
	      ]
	  },
	  {
	      "featureType": "landscape",
	      "elementType": "geometry",
	      "stylers": [
	          {
	              "hue": "#b7caaa"
	          },
	          {
	              "saturation": -14
	          },
	          {
	              "lightness": -18
	          },
	          {
	              "visibility": "on"
	          }
	      ]
	  },
	  {
	      "featureType": "landscape.man_made",
	      "elementType": "all",
	      "stylers": [
	          {
	              "hue": "#cbdac1"
	          },
	          {
	              "saturation": -6
	          },
	          {
	              "lightness": -9
	          },
	          {
	              "visibility": "on"
	          }
	      ]
	  },
	  {
	      "featureType": "road",
	      "elementType": "geometry",
	      "stylers": [
	          {
	              "hue": "#8d9b83"
	          },
	          {
	              "saturation": -89
	          },
	          {
	              "lightness": -12
	          },
	          {
	              "visibility": "on"
	          }
	      ]
	  },
	  {
	      "featureType": "road.highway",
	      "elementType": "geometry",
	      "stylers": [
	          {
	              "hue": "#d4dad0"
	          },
	          {
	              "saturation": -88
	          },
	          {
	              "lightness": 54
	          },
	          {
	              "visibility": "simplified"
	          }
	      ]
	  },
	  {
	      "featureType": "road.arterial",
	      "elementType": "geometry",
	      "stylers": [
	          {
	              "hue": "#bdc5b6"
	          },
	          {
	              "saturation": -89
	          },
	          {
	              "lightness": -3
	          },
	          {
	              "visibility": "simplified"
	          }
	      ]
	  },
	  {
	      "featureType": "road.local",
	      "elementType": "geometry",
	      "stylers": [
	          {
	              "hue": "#bdc5b6"
	          },
	          {
	              "saturation": -89
	          },
	          {
	              "lightness": -26
	          },
	          {
	              "visibility": "on"
	          }
	      ]
	  },
	  {
	      "featureType": "poi",
	      "elementType": "geometry",
	      "stylers": [
	          {
	              "hue": "#c17118"
	          },
	          {
	              "saturation": 61
	          },
	          {
	              "lightness": -45
	          },
	          {
	              "visibility": "on"
	          }
	      ]
	  },
	  {
	      "featureType": "poi.park",
	      "elementType": "all",
	      "stylers": [
	          {
	              "hue": "#8ba975"
	          },
	          {
	              "saturation": -46
	          },
	          {
	              "lightness": -28
	          },
	          {
	              "visibility": "on"
	          }
	      ]
	  },
	  {
	      "featureType": "transit",
	      "elementType": "geometry",
	      "stylers": [
	          {
	              "hue": "#a43218"
	          },
	          {
	              "saturation": 74
	          },
	          {
	              "lightness": -51
	          },
	          {
	              "visibility": "simplified"
	          }
	      ]
	  },
	  {
	      "featureType": "administrative.province",
	      "elementType": "all",
	      "stylers": [
	          {
	              "hue": "#ffffff"
	          },
	          {
	              "saturation": 0
	          },
	          {
	              "lightness": 100
	          },
	          {
	              "visibility": "simplified"
	          }
	      ]
	  },
	  {
	      "featureType": "administrative.neighborhood",
	      "elementType": "all",
	      "stylers": [
	          {
	              "hue": "#ffffff"
	          },
	          {
	              "saturation": 0
	          },
	          {
	              "lightness": 100
	          },
	          {
	              "visibility": "off"
	          }
	      ]
	  },
	  {
	      "featureType": "administrative.locality",
	      "elementType": "labels",
	      "stylers": [
	          {
	              "hue": "#ffffff"
	          },
	          {
	              "saturation": 0
	          },
	          {
	              "lightness": 100
	          },
	          {
	              "visibility": "off"
	          }
	      ]
	  },
	  {
	      "featureType": "administrative.land_parcel",
	      "elementType": "all",
	      "stylers": [
	          {
	              "hue": "#ffffff"
	          },
	          {
	              "saturation": 0
	          },
	          {
	              "lightness": 100
	          },
	          {
	              "visibility": "off"
	          }
	      ]
	  },
	  {
	      "featureType": "administrative",
	      "elementType": "all",
	      "stylers": [
	          {
	              "hue": "#3a3935"
	          },
	          {
	              "saturation": 5
	          },
	          {
	              "lightness": -57
	          },
	          {
	              "visibility": "off"
	          }
	      ]
	  },
	  {
	      "featureType": "poi.medical",
	      "elementType": "geometry",
	      "stylers": [
	          {
	              "hue": "#cba923"
	          },
	          {
	              "saturation": 50
	          },
	          {
	              "lightness": -46
	          },
	          {
	              "visibility": "on"
	          }
	      ]
	  }
	]

};

/* para buscar um endereco no  mapa */
function inicializarBuscaPorEndereco (){
	
	// não vou usar essa função no momento
	
	
	
	/* 
	 * retirei do index i input
	 * <input id="pac-input" class="controls" type="text" placeholder="Buscar endereço...">
	 * 
	 * se for usar, colocar no index, antes da div id="map" uma div assim: <div id="divInput"></div>
	 */

	/* criação do input por javascript */
	var inputUnit = document.createElement('input');
      inputUnit.setAttribute('type', 'text');
      inputUnit.setAttribute('id', 'pac-input');
     	inputUnit.setAttribute('name', 'buscarPorEndereco');
    	inputUnit.setAttribute('class', 'controls');
      inputUnit.setAttribute('placeholder', 'Buscar por endereco...');
      
      
			document.getElementById("divInput").appendChild(inputUnit);
			
  
 // Create the search box and link it to the UI element.
  var input = document.getElementById('pac-input');
  var searchBox = new google.maps.places.SearchBox(input);
 	map.controls[google.maps.ControlPosition.TOP_RIGHT].push(input);

  // Bias the SearchBox results towards current map's viewport.
  map.addListener('bounds_changed', function() {
    searchBox.setBounds(map.getBounds());
  });

  var markers = [];
  // Listen for the event fired when the user selects a prediction and retrieve
  // more details for that place.
  searchBox.addListener('places_changed', function() {
    var places = searchBox.getPlaces();

    if (places.length == 0) {
      return;
    }

    // Clear out the old markers.
    markers.forEach(function(marker) {
      marker.setMap(null);
    });
    markers = [];

    // For each place, get the icon, name and location.
    var bounds = new google.maps.LatLngBounds();
    places.forEach(function(place) {
      if (!place.geometry) {
        console.log("Returned place contains no geometry");
        return;
      }
      var icon = {
        url: place.icon,
        size: new google.maps.Size(71, 71),
        origin: new google.maps.Point(0, 0),
        anchor: new google.maps.Point(17, 34),
        scaledSize: new google.maps.Size(25, 25)
      };

      // Create a marker for each place.
      markers.push(new google.maps.Marker({
        map: map,
        icon: icon,
        title: place.name,
        position: place.geometry.location
      }));

      if (place.geometry.viewport) {
        // Only geocodes have viewport.
        bounds.union(place.geometry.viewport);
      } else {
        bounds.extend(place.geometry.location);
      }
    });
    map.fitBounds(bounds);
  });

}
