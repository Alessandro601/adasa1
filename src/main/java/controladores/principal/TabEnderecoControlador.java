package controladores.principal;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import dao.EnderecoDao;
import entidades.Demanda;
import entidades.Documento;
import entidades.Endereco;
import entidades.Interferencia;
import entidades.RA;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.Duration;
import mapas.GoogleMap;
import principal.Alerta;
import principal.Componentes;
import principal.FormatoData;

public class TabEnderecoControlador implements Initializable {
	
	// para trazer a demanda cadastrada e relacionado com o endereco //
	Demanda demanda;
	Endereco endereco = new Endereco();

	// recebimento da demanda e preenchimento na parte superior - DOCUMENTO: ... 
	public void setDemanda (Demanda demanda)  {
		
		this.demanda = demanda;
		// preencher o label com a demanda selecionada //
		
		if(!(demanda == null)) {
			lblDemanda.setText(
					demanda.getDemTipo() 
					+ ", Sei n° " + demanda.getDemNumeroSEI()
					+ ", Processo n° " + demanda.getDemProcesso()
					);
			
			lblDemanda.setStyle("-fx-text-fill: #4A4A4A;"); 
		} else {
			
			lblDemanda.setText(
					"Não há demanda relacionada a este endereco! "
					);
			lblDemanda.setStyle("-fx-text-fill: #FF0000;");
		}
	
	}

	Documento documento = new  Documento();
	
	public void setDocumento (Documento documento)  {
		
		this.documento = documento;
		// preencher o label com a demanda selecionada //
		
		if(!(documento == null)) {
			lblDemanda.setText(
					documento.getDocTipo()
					+ ", Sei n° " + documento.getDocSEI()
					+ ", Processo n° " + documento.getDocProcesso()
					);
			
			lblDemanda.setStyle("-fx-text-fill: #4A4A4A;"); 
		} else {
			
			lblDemanda.setText(
					"Não há demanda relacionada a este endereco! "
					);
			lblDemanda.setStyle("-fx-text-fill: #FF0000;");
		}
	
	}

	Pane pMap;

	Label lblDataAtualizacao = new Label();
	
	//-- TableView endereco --//
	private TableView <Endereco> tvLista = new TableView<>();

	TableColumn<Endereco, String> tcDesEnd = new TableColumn<>("Endereço");
	TableColumn<Endereco, String> tcEndRA = new TableColumn<>("Região Administrativa");
	TableColumn<Endereco, String> tcEndCid = new TableColumn<>("CEP");

	int intRA = 1;
	String strRA = "Plano Piloto";
	
	final int [] listaRA = new int [] {
			20,4,19,9,11,31,2,10,28,27,18,16,8,7,24,6,
			1,15,17,21,12,13,14,25,29,5,26,22,3,23,30,
	};
	
	ObservableList<String> olEndRA = FXCollections
	.observableArrayList(
			
			"Águas Claras"	,
			"Brazlândia"	,
			"Candangolândia"	,
			"Ceilândia"	,
			"Cruzeiro"	,
			"Fercal"	,
			"Gama"	,
			"Guará"	,
			"Itapoã"	,
			"Jardim Botânico"	,
			"Lago Norte"	,
			"Lago Sul"	,
			"Núcleo Bandeirante"	,
			"Paranoá"	,
			"Park Way"	,
			"Planaltina"	,
			"Plano Piloto"	,
			"Recanto das Emas"	,
			"Riacho Fundo"	,
			"Riacho Fundo II"	,
			"Samambaia"	,
			"Santa Maria"	,
			"São Sebastião"	,
			"SCIA/Estrutural"	,
			"SIA"	,
			"Sobradinho"	,
			"Sobradinho II"	,
			"Sudoeste/Octogonal"	,
			"Taguatinga"	,
			"Varjão"	,
			"Vicente Pires"	
			); 	
		
	ObservableList<String> olEndUF = FXCollections
				.observableArrayList("DF" , "GO", "Outro");

		Label lblEndereco = new Label();
		//-- string para chamar as coordenadas corretas do mapa --//
		String strHTMLMap;
		//-- String de pesquisa de enderecos --//
		String strPesquisa = "";
		
		ObservableList<Endereco> obsList = FXCollections.observableArrayList();
		
	public void habilitarEndereco () {
		
		tfLogradouro.setText("");
		
		cbRA.setValue(null);
		
		tfCEP.setText("");
		tfCidade.setText("Brasília");
		
		cbUF.setValue("DF");
		
		tfLatitude.setText("");
		tfLongitude.setText("");
		
		
		tfLogradouro.setDisable(false);
		cbRA.setDisable(false);
		
		
		tfCEP.setDisable(false);
		tfCidade.setDisable(false);
		cbUF.setDisable(false);
		tfLatitude.setDisable(false);
		tfLongitude.setDisable(false);
	
		btnSalvar.setDisable(false);
		btnEditar.setDisable(true);
		btnExcluir.setDisable(true);
		btnEditar.setDisable(true);
		
	}
	
	public void salvarEndereco () {
		
		if (tfLatitude.getText().isEmpty() || 
				tfLongitude.getText().isEmpty()) {
			
			Alerta a = new Alerta ();
			a.alertar(new Alert(Alert.AlertType.ERROR, "Coordenadas inválidas!!!", ButtonType.OK));
			
		} 
		
		else if (documento == null) {
				
			Alerta a = new Alerta ();
			a.alertar(new Alert(Alert.AlertType.ERROR, "Não há documento selecionado!!!", ButtonType.OK));
				
			} 
		
	else {
	
	
		if (tfLogradouro.getText().isEmpty()) {
			
			Alerta a = new Alerta ();
			a.alertar(new Alert(Alert.AlertType.ERROR, "Informe o logradouro do empreendimento!!!", ButtonType.OK));
			
		} else {
		
			RA ra = new RA ();
				ra.setRaID(intRA);
				ra.setRaNome(strRA);
	
			Endereco end = new Endereco();
		
			end.setEndLogradouro(tfLogradouro.getText());
			end.setEndRAFK(ra);
				
			end.setEndCEP(tfCEP.getText());
			end.setEndCidade(tfCidade.getText());
			end.setEndUF(cbUF.getValue());
		

			end.setEndDDLatitude(Double.parseDouble(tfLatitude.getText()));
			end.setEndDDLongitude(Double.parseDouble(tfLongitude.getText()));
			
			GeometryFactory geoFac = new GeometryFactory();
			
			Point p = geoFac.createPoint(new Coordinate(
					Double.parseDouble(tfLongitude.getText()),
					Double.parseDouble(tfLatitude.getText()
					)));
			
			p.setSRID(4674);
				
			end.setEndGeom(p);
			
			end.setEndAtualizacao(
					Timestamp.valueOf((LocalDateTime.now())));
			
				Documento doc = new Documento();
				
				doc = documento;
				doc.setDocEnderecoFK(end);
			
				// adicionar o documento editada //
				Set<Documento> docList = new HashSet<>();
				docList.add(doc);
				
				end.setDocumentos(docList);
				
				EnderecoDao endDao = new EnderecoDao();
					
					endDao.salvarEndereco(end); //solução para recuperar o id do endereço
					endDao.mergeEndereco(end); // assim adiciona o id end na demanda dem
				
					// levar o endereco salvo para a tabinterferencia //	
					if (intControlador == 0) {
					 TabInterferenciaControlador.controladorAtendimento.setEndereco(end);
					 TabUsuarioControlador.controladorAtendimento.setEndereco(end);
					 TabParecerControlador.controladorAtendimento.setEndereco(end);
			
				 	}
				     
					    if (intControlador == 1) {
					    	TabInterferenciaControlador.controladorFiscalizacao.setEndereco(end);
					    	TabUsuarioControlador.controladorFiscalizacao.setEndereco(end);
					    	TabParecerControlador.controladorFiscalizacao.setEndereco(end);
					    }
				    
						    if (intControlador == 2) {
						    	TabInterferenciaControlador.controladorOutorga.setEndereco(end);
						    	TabUsuarioControlador.controladorOutorga.setEndereco(end);
						    	TabParecerControlador.controladorOutorga.setEndereco(end);
						    }
							    
							
				//-- modular botoes--//
				modularBotoesInicial ();
				
				obsList.remove(end);
				obsList.add(end);
				
				modularBotoesInicial();
				
				Alerta a = new Alerta ();
				a.alertar(new Alert(Alert.AlertType.INFORMATION, "Cadastro salvo com sucesso!!!", ButtonType.OK));
							
					
			}
		}
	}
	
	public void editarEndereco () {
	
	
		if (tfLogradouro.isDisable()) {
			
			tfLogradouro.setDisable(false);
			cbRA.setDisable(false);
			tfCEP.setDisable(false);
			tfCidade.setDisable(false);
			cbUF.setDisable(false);
			tfLatitude.setDisable(false);
			tfLongitude.setDisable(false);
		
				
		} else {
		
			if (tfLatitude.getText().isEmpty()|| tfLongitude.getText().isEmpty() ) {
				
				Alerta a = new Alerta ();
				a.alertar(new Alert(Alert.AlertType.ERROR, "Coordenadas inválidas!!!", ButtonType.OK));
				// colocar para não aceitar texto e somente número
				} 
				
				else if (documento == null) {
					Alerta a = new Alerta ();
					a.alertar(new Alert(Alert.AlertType.ERROR, "Não foi selecionado um documento!!!", ButtonType.OK));
				}
		
				else {
					
					RA ra = new RA ();
					ra.setRaID(intRA);
					ra.setRaNome(strRA);
	
					Endereco end = new Endereco ();
					
					end = tvLista.getSelectionModel().getSelectedItem();
						
					end.setEndLogradouro(tfLogradouro.getText());
					end.setEndRAFK(ra);
					end.setEndCEP(tfCEP.getText());
					end.setEndCidade(tfCidade.getText());
					end.setEndUF(cbUF.getValue());
					
					end.setEndDDLatitude(Double.parseDouble(tfLatitude.getText()));
					end.setEndDDLongitude(Double.parseDouble(tfLongitude.getText()));
					
					GeometryFactory geoFac = new GeometryFactory();
					
					Point p = geoFac.createPoint(new Coordinate(
							Double.parseDouble(tfLongitude.getText()),
							Double.parseDouble(tfLatitude.getText()
							)));
					
					p.setSRID(4674);
						
					end.setEndGeom(p);
					
					end.setEndAtualizacao(Timestamp.valueOf((LocalDateTime.now())));
					
					Documento doc = new Documento();
					
					doc = documento;
					doc.setDocEnderecoFK(end);
					
					/* retirar na lista de demandas do endereco uma demanda repetida */
					Iterator<Documento> itDoc;
					
					Set<Documento> hashDoc = new HashSet<Documento>();
					hashDoc = end.getDocumentos();
					
					for (itDoc = hashDoc.iterator(); itDoc.hasNext();)
			        {
						Documento d = (Documento) itDoc.next();
			          if (d.getDocID() == doc.getDocID()) {
			        	  itDoc.remove();
			          }
			        }
					
					// adicionar a demanda editada //
					end.getDocumentos().add(doc);
					
					// dao //
					EnderecoDao enderecoDao = new EnderecoDao();
				
						enderecoDao.mergeEndereco(end);
					
					// levar o endereco salvo para a tabinterferencia //	

					// levar o endereco salvo para a tabinterferencia //	
					if (intControlador == 0) {
					 TabInterferenciaControlador.controladorAtendimento.setEndereco(end);
					 TabUsuarioControlador.controladorAtendimento.setEndereco(end);
					 TabParecerControlador.controladorAtendimento.setEndereco(end);
			
				 	}
					     
				    if (intControlador == 1) {
				    	TabInterferenciaControlador.controladorFiscalizacao.setEndereco(end);
				    	TabUsuarioControlador.controladorFiscalizacao.setEndereco(end);
				    	TabParecerControlador.controladorFiscalizacao.setEndereco(end);
				    }
					    
				    if (intControlador == 2) {
				    	TabInterferenciaControlador.controladorOutorga.setEndereco(end);
				    	TabUsuarioControlador.controladorOutorga.setEndereco(end);
				    	TabParecerControlador.controladorOutorga.setEndereco(end);
				    }
		
						    
					// atualizar a tableview //
					obsList.remove(end);
					obsList.add(end);
					
					modularBotoesInicial (); 
					
					Alerta a = new Alerta ();
					a.alertar(new Alert(Alert.AlertType.INFORMATION, "Cadastro editado com sucesso!!!", ButtonType.OK));
						
				}
	
	}	
	
}

	public void excluirEndereco () {
	
		Endereco end = tvLista.getSelectionModel().getSelectedItem();
		
		int id = end.getEndID();
		
		EnderecoDao endDao = new EnderecoDao();
		
			try {
				
				endDao.removerEndereco(id);
				
				 if (intControlador == 0) {
					 TabInterferenciaControlador.controladorAtendimento.setEndereco(null);
					 TabUsuarioControlador.controladorAtendimento.setEndereco(null);
			
				 	}
				     
				    if (intControlador == 1) {
				    	TabInterferenciaControlador.controladorFiscalizacao.setEndereco(null);
				    	TabUsuarioControlador.controladorFiscalizacao.setEndereco(null);
				    }
				    
				    if (intControlador == 2) {
				    	TabInterferenciaControlador.controladorOutorga.setEndereco(null);
				    	TabUsuarioControlador.controladorOutorga.setEndereco(null);
				    	TabParecerControlador.controladorOutorga.setEndereco(null);
				    }
				
				obsList.remove(end);
				
				modularBotoesInicial();
				
				Alerta a = new Alerta ();
				a.alertar(new Alert(Alert.AlertType.INFORMATION, "Cadastro deletado com sucesso!!!", ButtonType.OK));
				
		
					}
		
					catch (Exception e) {
						
						Alerta a = new Alerta ();
						a.alertar(new Alert(Alert.AlertType.ERROR, "Há denúncia associada a este endereço!", ButtonType.OK));
						
					}
		
}

	public void cancelarEndereco () {
	
		modularBotoesInicial ();
		
		tfLogradouro.setText("");
		
		cbRA.setValue(null);
		
		tfCEP.setText("");
		
		cbUF.setValue(null);
		
		tfLatitude.setText("");
		tfLongitude.setText("");
	
}

	public void pesquisarEndereco () {
	
		strPesquisa = tfPesquisar.getText();
		
		listarEnderecos (strPesquisa);
	
		modularBotoesInicial (); 
	
}

	/* capturar as coordenadas clicadas no mapa e trazer para o cadastro do endereco */
	public void capturarLatitudeLongitude () {
		
		tfLatitude.setText( ControladorPrincipal.capturarGoogleMaps().getLat() );
		tfLongitude.setText( ControladorPrincipal.capturarGoogleMaps().getLon());
		
	}

	// pane para o mapa
	Pane pEnderecoMapa = new Pane();
	
	Pane pDemanda;
	Label lblDemanda;
		Button btnDemanda;

			ArrayList<Node> listNodesDemanda = new ArrayList<Node>();
			
			Pane pDadosEndereco;
			TextField tfLogradouro;
				ComboBox<String> cbRA;
					TextField tfCEP;
						TextField tfCidade;
							ComboBox<String> cbUF;
								TextField tfLatitude;
									TextField tfLongitude;
										Button btnLatLon;
									
										ArrayList<Node> listNodeDadosEndereco = new ArrayList<Node>();
								
										 Pane pPersistencia;
										  	Button btnNovo;
										  		Button btnSalvar;
										  			Button btnEditar;
										  				Button btnExcluir;
										  					Button btnCancelar;
										  						Button btnPesquisar;
										  						
										  							TextField tfPesquisar;
										  							
										  								ArrayList<Node> listNodesPersistencia= new ArrayList<Node>();
										  								
		
	public static TabEnderecoControlador controladorAtendimento;
		public static TabEnderecoControlador controladorFiscalizacao;
			public static TabEnderecoControlador controladorOutorga;
			
			int intControlador;
	
	/* transmitir valores entre as tabs 
	 * @controladorAtendimento - caso o atendimento seja chamado
	 * @controladorFiscalizacao - caso a fiscalizacao seja chamada
	 * 		Resolve o problema de chamar as duas abas (atendimento e fiscalizacao) e os valores se confundirem ente elas
	 */
	public TabEnderecoControlador (int i) {
		
		if (i==0) {
			controladorAtendimento = this;
			intControlador = i;
		}
		if(i==1) {
			controladorFiscalizacao = this;
			intControlador = i;
		}
		
		if(i==2) {
			controladorOutorga = this;
			intControlador = i;
		}
	
	}
	
	@FXML Pane pEndereco;
	  
	Pane p1 = new Pane();
	BorderPane bp1 = new BorderPane();
	BorderPane bp2 = new BorderPane();
	ScrollPane sp = new ScrollPane();
	Pane pMapa = new Pane();
	  
	/* array de posicoes prefWidth prefHeight Layout Y e X */
	Double prefSizeWHeLayXY [][];
		
	Componentes com;
	
	Pane pPrincipal = new Pane();
	  
	public void initialize(URL url, ResourceBundle rb) {
		
		 bp1.minWidthProperty().bind(pEndereco.widthProperty());
		    bp1.maxHeightProperty().bind(pEndereco.heightProperty().subtract(60));
		    
		    bp1.getStyleClass().add("border-pane");
		    
		    bp2.setPrefHeight(800.0D);
		    bp2.minWidthProperty().bind(pEndereco.widthProperty());
		    
		    sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		    
		    sp.setContent(bp2);
		    
		    bp1.setCenter(sp);
		    
		    pEndereco.getChildren().add(bp1);
		    
		    p1.setMaxSize(980.0, 1000.0);
		    p1.setMinSize(980.0, 1000.0);
		    
		    bp2.setTop(p1);
		    BorderPane.setAlignment(p1, Pos.CENTER);
		    
		    listNodesDemanda.add(pDemanda = new Pane());
			listNodesDemanda.add(new Label("DEMANDA:"));
				listNodesDemanda.add(lblDemanda = new Label());
					listNodesDemanda.add(btnDemanda = new Button("<<<"));
	    
	    prefSizeWHeLayXY = new Double [][] { 
	    	{950.0,60.0,15.0,10.0},
	    	{75.0,30.0,27.0,15.0},
	    	{740.0,30.0,109.0,15.0},
	    	{65.0,25.0,859.0,17.0},
    				};
	    	
	    com = new Componentes();
	    com.popularTela(listNodesDemanda, prefSizeWHeLayXY, p1);
	    
	    
	    listNodeDadosEndereco.add(pDadosEndereco = new Pane());
	    	listNodeDadosEndereco.add(new Label("ENDEREÇO DO EMPREENDIMENTO:"));
	    	listNodeDadosEndereco.add(tfLogradouro = new TextField());
	    		listNodeDadosEndereco.add(new Label("REGIÃO ADMINISTRATIVA:"));
	    		listNodeDadosEndereco.add(cbRA = new  ComboBox<>());
	    			listNodeDadosEndereco.add(new Label("CEP:"));
	    			listNodeDadosEndereco.add(tfCEP = new TextField());
					    listNodeDadosEndereco.add(new Label("CIDADE:"));
					    listNodeDadosEndereco.add(tfCidade = new TextField());
						    listNodeDadosEndereco.add(new Label("UF:"));
							listNodeDadosEndereco.add(cbUF = new  ComboBox<>());
								listNodeDadosEndereco.add(new Label("LATITUDE (Y):"));
							    listNodeDadosEndereco.add(tfLatitude = new TextField());
							    	listNodeDadosEndereco.add(new Label("LONGITUDE(X):"));
								    listNodeDadosEndereco.add(tfLongitude = new TextField());
								    	listNodeDadosEndereco.add(btnLatLon = new Button());
	    
	
		    prefSizeWHeLayXY = new Double [][] { 
		    	{930.0,130.0,25.0,85.0},
		    	{425.0,30.0,15.0,5.0},
		    	{420.0,30.0,15.0,35.0},
		    	{165.0,30.0,445.0,5.0},
		    	{165.0,30.0,445.0,35.0},
		    	{100.0,30.0,620.0,5.0},
		    	{100.0,30.0,620.0,35.0},
		    	{100.0,30.0,730.0,5.0},
		    	{100.0,30.0,730.0,35.0},
		    	{60.0,30.0,840.0,5.0},
		    	{75.0,30.0,840.0,35.0},
		    	{110.0,30.0,180.0,80.0},
		    	{140.0,30.0,300.0,80.0},
		    	{110.0,30.0,450.0,80.0},
		    	{140.0,30.0,570.0,80.0},
		    	{25.0,25.0,725.0,83.0},
		    };
		    	
		    com = new Componentes();
		    com.popularTela(listNodeDadosEndereco, prefSizeWHeLayXY, p1);
	    
	    cbRA.setItems(olEndRA);
	    cbUF.setItems(olEndUF);
	    
	    
	    listNodesPersistencia.add(pPersistencia = new Pane());
	    listNodesPersistencia.add(btnNovo = new Button("NOVO"));
	    listNodesPersistencia.add(btnSalvar = new Button("SALVAR"));
	    listNodesPersistencia.add(btnEditar = new Button("EDITAR"));
	    listNodesPersistencia.add(btnExcluir = new Button("EXCLUIR"));
	    listNodesPersistencia.add(btnCancelar = new Button("CANCELAR"));
	    listNodesPersistencia.add(tfPesquisar = new TextField());
	    listNodesPersistencia.add(btnPesquisar = new Button("PESQUISAR"));
	    
			    prefSizeWHeLayXY = new Double[][] { 
			    	{930.0,60.0,25.0,230.0},
			    	{95.0,25.0,18.0,18.0},
			    	{95.0,25.0,123.0,18.0},
			    	{95.0,25.0,228.0,18.0},
			    	{95.0,25.0,333.0,18.0},
			    	{95.0,25.0,438.0,18.0},
			    	{265.0,25.0,543.0,18.0},
			    	{95.0,25.0,818.0,18.0},
			    };
							    				
				 com = new Componentes();
				    com.popularTela(listNodesPersistencia, prefSizeWHeLayXY, p1);    
		    
	    
	    // para trazer o valor da entidade principal, no caso Endereco
	    tcDesEnd.setCellValueFactory(new PropertyValueFactory<Endereco, String>("endLogradouro"));
		// para trazer valor de outra entidade, no caso RA
	    tcEndRA.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Endereco, String>, ObservableValue<String>>() {
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Endereco, String> e) {
		    	return new SimpleStringProperty(e.getValue().getEndRAFK().getRaNome());
		       
		    }
		});
		
		tcDesEnd.setPrefWidth(440);
		tcEndRA.setPrefWidth(232);
		tcEndCid.setPrefWidth(232);
			
	    tvLista.getColumns().add(tcDesEnd);
	    	tvLista.getColumns().add(tcEndRA); 
	    		tvLista.getColumns().add(tcEndCid); 
	    			tvLista.setItems(obsList);
	    
					    tvLista.setPrefSize(930, 185);
							tvLista.setLayoutX(25);
								tvLista.setLayoutY(305);
								
	    
	    lblDataAtualizacao.setPrefSize(247, 22);
	    lblDataAtualizacao.setLayoutX(705);
	    lblDataAtualizacao.setLayoutY(500);
	  
	    
		// para trazer o valor da entidade principal, no caso Endereco
		//tcEndCid.setCellValueFactory(new PropertyValueFactory<Endereco, String>("endCEP")); // endCEP
	    
	    cbRA.setItems(olEndRA);
	    cbRA.setValue("Plano Piloto");
	    cbUF.setItems(olEndUF);
	    
	    
	    pEnderecoMapa.setPrefSize(930, 400);
	    pEnderecoMapa.setLayoutX(25);
	    pEnderecoMapa.setLayoutY(530);
	    pEnderecoMapa.getChildren().add(googleMaps);
	    
	    googleMaps.setWidth(930);
	    googleMaps.setHeight(400);
	    googleMaps.switchHybrid();
	    
	    p1.getChildren().addAll(tvLista,lblDataAtualizacao, pEnderecoMapa);
	    
	    cbRA.getSelectionModel().selectedIndexProperty().addListener(new
	            ChangeListener<Number>() {
	    	public void changed(@SuppressWarnings("rawtypes") ObservableValue ov,
	    		Number value, Number new_value) {
	    		
	    		if ( (Integer) new_value !=  -1)
	    		intRA = listaRA[(int) new_value];
	    		
	    		System.out.println("região adminsitrativa TABEndereco " + intRA);
	    		System.out.println("região adminsitrativa TABEndereco " + strRA);
	    		
            }
	    });
	    
	    cbRA.getSelectionModel()
	    	.selectedItemProperty()
	    	.addListener( 
	    	(ObservableValue<? extends String> observable, String oldValue, String newValue) ->
	    	
	    	strRA = (String) newValue 
	    );
	    
	    habilitarAcoesDosBotoes ();
	    // ao abrir fechar os campos para edicao //
	    modularBotoesInicial();
	    // ativar na tableview a possibilidade e selecionar uma opcao //
	    selecionarEndereco();
	   
	    	
	}
	
	/* ativar metodo onAction para os botoes */
	
	public void habilitarAcoesDosBotoes () {
		
	    btnNovo.setOnAction(new EventHandler<ActionEvent>() {

	        @Override
	        public void handle(ActionEvent event) {
	        	habilitarEndereco ();
	        }
	    });
		    
	    btnSalvar.setOnAction(new EventHandler<ActionEvent>() {

	        @Override
	        public void handle(ActionEvent event) {
	            salvarEndereco();
	        }
	    });
	    
	    btnEditar.setOnAction(new EventHandler<ActionEvent>() {

	        @Override
	        public void handle(ActionEvent event) {
	           editarEndereco();
	        }
	    });
	    
	    btnCancelar.setOnAction(new EventHandler<ActionEvent>() {

	        @Override
	        public void handle(ActionEvent event) {
	            cancelarEndereco();
	        }
	    });
	    
	    btnPesquisar.setOnAction(new EventHandler<ActionEvent>() {

	        @Override
	        public void handle(ActionEvent event) {
	            pesquisarEndereco();
	        }
	    });
	    
	    btnExcluir.setOnAction(new EventHandler<ActionEvent>() {

	        @Override
	        public void handle(ActionEvent event) {
	            excluirEndereco();
	        }
	    });
	    
	    btnLatLon.setOnAction(new EventHandler<ActionEvent>() {

	        @Override
	        public void handle(ActionEvent event) {
	        	capturarLatitudeLongitude ();
	        }
	    });
	    
	    btnDemanda.setOnAction(new EventHandler<ActionEvent>() {

	        @Override
	        public void handle(ActionEvent event) {
	        	
	            inicializarTelaDemanda();
	            TelaDocumentoControlador.telaDocCon.setEndereco(endereco);
	          //  TelaDemandaControlador.telaDemCon.setEndereco(endereco);
	            
	        }
	    });
	    
	   
		
	}
	
	TranslateTransition transDireita;
	TranslateTransition transEsquerda;
	Pane pTelaDemanda;
	Double dblTransicaoDemanda;
	  
	public void inicializarTelaDemanda () {
		  
	    if (pTelaDemanda == null) {
	    	
	    	pTelaDemanda = new Pane();
	    	pTelaDemanda.setPrefSize(500.0, 500.0);
	
	    	Pane p = new Pane();
	    	
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/principal/TelaDocumento.fxml"));
				loader.setRoot(p);
					loader.setController(new TelaDocumentoControlador(intControlador));
		
			try {
				loader.load();
			}
				catch (IOException e)	{
					System.out.println("erro leitura do pane");
					e.printStackTrace();
				}
		
			pTelaDemanda.getChildren().add(p);
			
			p1.getChildren().add(pTelaDemanda);
		
				transEsquerda = new TranslateTransition(new Duration(350.0), pTelaDemanda);
					transEsquerda.setToX(15.0);
				  
				transDireita = new TranslateTransition(new Duration(350.0), pTelaDemanda);
					transDireita.setToX(1300.0);
		  
					pTelaDemanda.setTranslateX(1300.0);
		  
		}
	    
	    movimentarTelaDemanda (15.0);
	    
	  }
	  
	public void movimentarTelaDemanda (Double dbltransEsquerda)	{
		  
		  
		  /*
	    if (demanda.getDemID() == 0) {
	    	
	      lbl_TP_Demanda.setText("Não há demanda selecionada!!!");
	      lbl_TP_Demanda.setTextFill(Color.RED);
	      
	    }
	    else {
	    	
	      lbl_TP_Demanda.setText(demanda
	        .getDemDocumento() + ", Sei nº" + demanda
	        	.getDemDocumentoSEI() + ", Processo nº " + demanda
	        		.getDemProcessoSEI());
	      
	      	lbl_TP_Demanda.setTextFill(Color.BLACK);
	    }*/
	    
		  dblTransicaoDemanda = Double.valueOf(pTelaDemanda.getTranslateX());
	    
	    if (dblTransicaoDemanda.equals(dbltransEsquerda)) {
	    	transDireita.play();
	    } else {
	    		transEsquerda.play();
	    }
	    
	  }

	//-- Modular os botoes na inicializacao do programa --//
	private void modularBotoesInicial () {
		
		tfLogradouro.setDisable(true);
		cbRA.setDisable(true);
		tfCEP.setDisable(true);
		tfCidade.setDisable(true);
		cbUF.setDisable(true);
		tfLatitude.setDisable(true);
		tfLongitude.setDisable(true);
		
		btnSalvar.setDisable(true);
		btnEditar.setDisable(true);
		btnExcluir.setDisable(true);
		btnNovo.setDisable(false);
		
	}
	
	// --- metodo para listar endereco --- //
	public void listarEnderecos (String strPesquisa) {
	 		
	 	// --- conexao - listar enderecos --- //
		EnderecoDao enderecoDao = new EnderecoDao();
		List<Endereco> enderecoList = enderecoDao.listarEndereco(strPesquisa);
		
		if (!obsList.isEmpty()) {
			obsList.clear();
		}
		
		obsList.addAll(enderecoList);
	}
	
	GoogleMap googleMaps = new GoogleMap();
	
	// método selecionar endereço -- //
	public void selecionarEndereco () {
		
			tvLista.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
				
				public void changed(ObservableValue<?> observable , Object oldValue, Object newValue) {
				
				Endereco end = (Endereco) newValue;
				
				if (end == null) {
					
					tfLogradouro.setText("");
					
					tfCEP.setText("");
					tfCidade.setText("");
					tfLatitude.setText("");
					tfLongitude.setText("");
					
					btnNovo.setDisable(true);
					btnSalvar.setDisable(true);
					btnEditar.setDisable(false);
					btnExcluir.setDisable(false);
					btnCancelar.setDisable(false);
					
				} else {
					
					endereco = end;

					// -- preencher os campos -- //
					tfLogradouro.setText(end.getEndLogradouro());
					
					cbRA.setValue(end.getEndRAFK().getRaNome()); 
					
					tfCEP.setText(end.getEndCEP());
					tfCidade.setText(end.getEndCidade());
					
					cbUF.setValue(end.getEndUF());
					
					tfLatitude.setText(end.getEndDDLatitude().toString());
					tfLongitude.setText(end.getEndDDLongitude().toString());
					
					// -- habilitar e desabilitar botoes -- //
					btnNovo.setDisable(true);
					btnSalvar.setDisable(true);
					btnEditar.setDisable(false);
					btnExcluir.setDisable(false);
					btnCancelar.setDisable(false);
					
					// mostrar data de atualizacao //
					FormatoData d = new FormatoData();
					try {lblDataAtualizacao.setText("Data de Atualização: " + d.formatarData(end.getEndAtualizacao()));
							lblDataAtualizacao.setTextFill(Color.BLACK);
					}catch (Exception e) {lblDataAtualizacao.setText("Não há data de atualização!");
							lblDataAtualizacao.setTextFill(Color.RED);}
					
					
					// levar o endereco salvo para a tabinterferencia //	
					

					// levar o endereco salvo para a tabinterferencia //	
					if (intControlador == 0) {
					 TabInterferenciaControlador.controladorAtendimento.setEndereco(end);
					 TabUsuarioControlador.controladorAtendimento.setEndereco(end);
					 TabParecerControlador.controladorAtendimento.setEndereco(end);
			
				 	}
				     
				    if (intControlador == 1) {
				    	TabInterferenciaControlador.controladorFiscalizacao.setEndereco(end);
				    	TabUsuarioControlador.controladorFiscalizacao.setEndereco(end);
				    	TabParecerControlador.controladorFiscalizacao.setEndereco(end);
				    }
				    
				    if (intControlador == 2) {
				    	TabInterferenciaControlador.controladorOutorga.setEndereco(end);
				    	TabUsuarioControlador.controladorOutorga.setEndereco(end);
				    	TabParecerControlador.controladorOutorga.setEndereco(end);
				    }
					
					/* caso não haja demanda relacionada ao endereco, setar demanda vazia */
					if (end.getDocumentos().size() == 0) {
						setDocumento (null);
						
						System.out.println("lista de demandas size " + end.getDocumentos().size());
					}
					
					/* havendo demandas, setar uma delas no lblDemanda */
					for (Documento doc : end.getDocumentos()) {
						
						setDocumento(doc);
						
					}
					
					// listar as interferencias
					List<Interferencia> iList = end.getInterferencias();
						
							// preparar strings para transmitir para o javascript pelo metodo 'setEnderecoInterferencias()'
							String strInterferencias = "";
					
							String strEndereco = end.getEndDDLatitude() + "," + end.getEndDDLongitude();
							/* string para os detalhes das  interferencias como tipo 
							de interferencia, bacia hid, uh e situação do processo */
							String strDetalhes = "";
							
							/* concatenacao de strings para levar dados das interferencias para o mapa */
							for(Interferencia i : iList) {
								
								strInterferencias += "|" + i.getInterDDLatitude() + "," + i.getInterDDLongitude() ;
								
								strDetalhes += "|" + i.getInterTipoInterferenciaFK().getTipoInterDescricao() 
												+ "," + i.getInterBaciaFK().getBaciaNome()
												+ "," + i.getInterUHFK().getUhID()
												+ "," +  i.getInterSituacaoProcessoFK().getSituacaoProcessoDescricao();
								
							} // fim loop for
					
					/* chamar os metodo necessarios, primeiro as coordenadas e detalhes, 
						zoom do mapa e deois centralizar o mapa de acordo com o endereco
						*/
					googleMaps.setEnderecoInterferencias(strEndereco, strInterferencias, strDetalhes);
					googleMaps.setZoom (15);
					googleMaps.setMapCenter(end.getEndDDLatitude(), end.getEndDDLongitude());
					
				}
				
				}
				
			});
			
		}
	
}

