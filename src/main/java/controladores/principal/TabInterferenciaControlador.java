package controladores.principal;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import dao.InterferenciaDao;
import entidades.BaciasHidrograficas;
import entidades.Endereco;
import entidades.Interferencia;
import entidades.SituacaoProcesso;
import entidades.Subterranea;
import entidades.Superficial;
import entidades.TipoAto;
import entidades.TipoInterferencia;
import entidades.TipoOutorga;
import entidades.UnidadeHidrografica;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import principal.Alerta;
import principal.Componentes;
import principal.FormatoData;

public class TabInterferenciaControlador  implements Initializable{
	
 	TabSubterraneaController tabSubCon;
	TabSuperficialController tabSupCon;
 	
 	Endereco endereco = new Endereco ();
 
	public void setEndereco (Endereco endereco) {
		
		this.endereco = endereco;
		// preencher o label com a demanda selecionada //
		
		lblEndereco.setText(
				
				endereco.getEndLogradouro()
				+ ", CEP n°: " + endereco.getEndCEP()
				+ ", Cidade: " + endereco.getEndCidade()
				
				);
			
	}
	
	ObservableList<Interferencia> obsList = FXCollections.observableArrayList();
	
	int tipoCaptacao = 3;
	
	
	//Button btnEndCoord;
	//Image imgEndCoord = new Image(TabVistoriaController.class.getResourceAsStream("/images/mapCoord.png"));

	//Button btnEndCoordMap = new Button();
	//Image imgEndCoordMap = new Image(TabVistoriaController.class.getResourceAsStream("/images/mapCoord.png"));
	
	//Image imgGetCoord = new Image(TabEnderecoController.class.getResourceAsStream("/images/getCoord.png")); 
	Button btnBuscarEnd = new Button ();

	//-- TableView Endereco --//
	private TableView <Interferencia> tvLista = new TableView<>();
	
	TableColumn<Interferencia, String> tcTipoInterferencia  = new TableColumn<>("Tipo de Interferência");
	TableColumn<Interferencia, String> tcLogradouro  = new TableColumn<>("Endereço do Empreendimento");
	TableColumn<Interferencia, String> tcSituacao  = new TableColumn<>("Situação");
	
	Label lblDataAtualizacao = new Label();
												
	int tipoInterferenciaID = 1;
	String tipoInterferenciaDescricao = "Superficial";
	final int [] listaTipoInterID = new int [] { 1,2,3,4,5,6,7 };
	
	int baciaID = 1;
	final int [] listaBaciasID = new int [] { 1,2,3,4,5,6,7,8 };
	
	int unidHidID = 1;
	final int [] listaUHID = new int [] { 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,
			22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41};
	
	int tipoOutorgaID = 1;
	final int [] listaTipoOutorgaID = new int [] { 1,2,3,4,5,6,7,8,9,10,11,12,13,14 };
	
	int tipoAtoID = 1;
	final int [] listaTipoAtoID = new int [] { 1,2,3,4,5,6 };
	
	int situacaoProcessoID = 1;
	final int [] listaSituacaoProcessoID = new int [] { 1,2,3,4,5,6,7,8 };
	

	public void btnNovoHab () {
					
		cbTipoInterferencia.setDisable(false);
		cbTipoOutorga.setDisable(false);
		cbSubtipoOutorga.setDisable(false);
		cbTipoAto.setDisable(false);
		cbSituacao.setDisable(false);
		
		dpDataPublicacao.setDisable(false);
		dpDataVencimento.setDisable(false);
		tfNumeroAto.setDisable(false);
		tfProcesoOutorga.setDisable(false);
		tfDespachoOutorga.setDisable(false);
		
		btnNovo.setDisable(true);
		btnSalvar.setDisable(false);
		btnEditar.setDisable(true);
		btnExcluir.setDisable(true);
		tfPesquisar.setDisable(false);
		
		btnPesquisar.setDisable(false);
	
		//cbTipoInterferencia.setItems(olTipoInterferencia);

					
	}

	public void btnSalvarHab () {
		
		TipoInterferencia tipoInterferencia = new TipoInterferencia();
		tipoInterferencia.setTipoInterID(tipoInterferenciaID);

		//BaciasHidrograficas baciaHid = new BaciasHidrograficas();
		//baciaHid.setBaciaID(baciaID);
		
		//UnidadeHidrografica UniHid = new UnidadeHidrografica();
		//UniHid.setUhID(unidHidID);
		
		TipoOutorga tipoOutorga = new TipoOutorga();
		tipoOutorga.setTipoOutorgaID(tipoOutorgaID);
		
		TipoAto tipoAto = new TipoAto();
		tipoAto.setTipoAtoID(tipoAtoID);
		
		SituacaoProcesso situacaoProcesso = new  SituacaoProcesso();
		situacaoProcesso.setSituacaoProcessoID(situacaoProcessoID);
		
		// coloca if combobox nulo ou algo assim
		if (endereco == null
				) { // ver de aceitar somente número 
			
				Alerta a = new Alerta ();
				a.alertar(new Alert(Alert.AlertType.ERROR,"Coordenadas inválidas!!!", ButtonType.OK));
			
		}
		
			else if (endereco == null) { // colocar na tabela que não pode ser nulo o id do endereco
				
				Alerta a = new Alerta ();
				a.alertar(new Alert(Alert.AlertType.ERROR,"Endereço relacionado não selecionado!!!", ButtonType.OK));
				
			
				} else {
				
						if (tipoInterferenciaID == 2) {
							
										if (tabSubCon.obterSubterranea().getSubTipoPocoFK() == null ||
											tabSubCon.obterSubterranea().getSubCaesb() == null ||
												tabSubCon.obterSubterranea().getSubSubSistemaFK() == null
											) {
										
										Alerta a = new Alerta ();
										a.alertar(new Alert(Alert.AlertType.ERROR,"Informe: Tipo de Captação (), Área é atendida pela Caesb() e Subsistema()!!!", ButtonType.OK));
										
										} else {
											
										Subterranea sub = new Subterranea ();
										
											sub = tabSubCon.obterSubterranea();
											
											sub.setInterTipoInterferenciaFK(tipoInterferencia);
											
											sub.setInterTipoOutorgaFK(tipoOutorga);
											sub.setInterTipoAtoFK(tipoAto);
											sub.setInterSituacaoProcessoFK(situacaoProcesso);
											
											sub.setIntAtualizacao(Timestamp.valueOf((LocalDateTime.now())));
											
											sub.setInterDataPublicacao(Date.valueOf(dpDataPublicacao.getValue()));
											sub.setInterDataVencimento(Date.valueOf(dpDataVencimento.getValue()));
											
											sub.setInterNumeroAto(tfNumeroAto.getText());
											
											sub.setInterProcRenovacao(tfProcesoOutorga.getText());
											sub.setInterDespachoRenovacao(tfDespachoOutorga.getText());
											
											sub.setInterEnderecoFK(endereco);
												/*
												GeometryFactory geoFac = new GeometryFactory();
												
												Point p = geoFac.createPoint(new Coordinate(
														Double.parseDouble(tfLon.getText()),
														Double.parseDouble(tfLat.getText()
														)));
												
												p.setSRID(4674);
													
												sub.setInterGeom(p);
												*/
											
											InterferenciaDao interferenciaDao = new InterferenciaDao ();
											
											interferenciaDao.salvaInterferencia(sub);	
								
											obsList.remove(sub);
											obsList.add(sub);
											
										modularBotoes ();
										
										Alerta a = new Alerta ();
										a.alertar(new Alert(Alert.AlertType.INFORMATION, "Interferência salva com sucesso!", ButtonType.OK));
								
										
										}
									
								} // fim subterranea
						
						
						else if (tipoInterferenciaID == 1) {
							
								if (
										tabSupCon.obterSuperficial() == null
										
										) {
									
									Alerta a = new Alerta ();
									a.alertar(new Alert(Alert.AlertType.ERROR, "Informe o Local de Captação e se há Caesb!!!", ButtonType.OK));
									
									} else {
									
											Superficial sup = new Superficial ();
											
												sup = tabSupCon.obterSuperficial();
												
												sup.setInterTipoInterferenciaFK(tipoInterferencia);
												//sup.setInterBaciaFK(baciaHid);
												//sup.setInterUHFK(UniHid);
												sup.setInterTipoOutorgaFK(tipoOutorga);
												sup.setInterTipoAtoFK(tipoAto);
												sup.setInterSituacaoProcessoFK(situacaoProcesso);
												
												//sup.setInterDDLatitude(Double.parseDouble(tfLat.getText()));
												//sup.setInterDDLongitude(Double.parseDouble(tfLon.getText()));
													
												sup.setIntAtualizacao(Timestamp.valueOf((LocalDateTime.now())));
												
												sup.setInterDataPublicacao(Date.valueOf(dpDataPublicacao.getValue()));
												sup.setInterDataVencimento(Date.valueOf(dpDataVencimento.getValue()));
												
												sup.setInterNumeroAto(tfNumeroAto.getText());
												
												sup.setInterProcRenovacao(tfProcesoOutorga.getText());
												sup.setInterDespachoRenovacao(tfDespachoOutorga.getText());
												
												sup.setInterEnderecoFK(endereco);
													
												/*
													GeometryFactory geoFac = new GeometryFactory();
													
													Point p = geoFac.createPoint(new Coordinate(
															Double.parseDouble(tfLon.getText()),
															Double.parseDouble(tfLat.getText()
															)));
													
													p.setSRID(4674);
														
													sup.setInterGeom(p);
													*/
												
											InterferenciaDao interferenciaDao = new InterferenciaDao ();
											interferenciaDao.salvaInterferencia(sup);
											
											obsList.remove(sup);
											obsList.add(sup);
											
											modularBotoes ();
											
											Alerta a = new Alerta ();
											a.alertar(new Alert(Alert.AlertType.INFORMATION, "Interferência salva com sucesso!", ButtonType.OK));
											
											}
											
												
									} // fim superficial //
						
						else {
							
							Interferencia inter = new Interferencia();
							
							inter.setInterTipoInterferenciaFK(tipoInterferencia);
							//inter.setInterBaciaFK(baciaHid);
							//inter.setInterUHFK(UniHid);
							inter.setInterTipoOutorgaFK(tipoOutorga);
							inter.setInterTipoAtoFK(tipoAto);
							inter.setInterSituacaoProcessoFK(situacaoProcesso);
							
							//inter.setInterDDLatitude(Double.parseDouble(tfLat.getText()));
							//inter.setInterDDLongitude(Double.parseDouble(tfLon.getText()));
							
							inter.setInterNumeroAto(tfNumeroAto.getText());
							
							inter.setInterProcRenovacao(tfProcesoOutorga.getText());
							inter.setInterDespachoRenovacao(tfDespachoOutorga.getText());
							
							inter.setIntAtualizacao(Timestamp.valueOf((LocalDateTime.now())));
						
							inter.setInterEnderecoFK(endereco);
							
							/*
							GeometryFactory geoFac = new GeometryFactory();
							
							Point p = geoFac.createPoint(new Coordinate(
									Double.parseDouble(tfLon.getText()),
									Double.parseDouble(tfLat.getText()
									)));
							
							p.setSRID(4674);
								
							inter.setInterGeom(p);
							*/
							
							
							InterferenciaDao interferenciaDao = new InterferenciaDao ();
							interferenciaDao.salvaInterferencia(inter);
							
								obsList.remove(inter);
								obsList.add(inter);
								
									
								//tvLista.setItems(obsList); 
							
								modularBotoes ();
								
								//-- Alerta de endereco salvo --//
								Alerta a = new Alerta ();
								a.alertar(new Alert(Alert.AlertType.INFORMATION, "Interferência salva com sucesso!", ButtonType.OK));
							
							
						} // fim outras interferencias
						
				}
		
		}
	
	//-- botao editar --//
	public void btnEditarHab () {
		
		TipoInterferencia tipoInterferencia = new TipoInterferencia();
		tipoInterferencia.setTipoInterID(tipoInterferenciaID);

		BaciasHidrograficas baciaHid = new BaciasHidrograficas();
		baciaHid.setBaciaID(baciaID);
		
		UnidadeHidrografica UniHid = new UnidadeHidrografica();
		UniHid.setUhID(unidHidID);
		
		TipoOutorga tipoOutorga = new TipoOutorga();
		tipoOutorga.setTipoOutorgaID(tipoOutorgaID);
		
		TipoAto tipoAto = new TipoAto();
		tipoAto.setTipoAtoID(tipoAtoID);
		
		SituacaoProcesso situacaoProcesso = new  SituacaoProcesso();
		situacaoProcesso.setSituacaoProcessoID(situacaoProcessoID);
		
		
		// ver excecao de querer editar sem esconlher o endereco da interferencia...
		
		// habilitar os campos para edição //
		if (cbTipoInterferencia.isDisable()) {
					
			cbTipoInterferencia.setDisable(false);
			cbTipoOutorga.setDisable(false);
			cbSubTipoOutorga.setDisable(false);
			cbTipoAto.setDisable(false);
			cbSituacao.setDisable(false);
			
			
			dpDataPublicacao.setDisable(false);
			dpDataVencimento.setDisable(false);
			tfNumeroAto.setDisable(false);
			tfProcesoOutorga.setDisable(false);
			tfDespachoOutorga.setDisable(false);	
			
		}
		
		// arrumar if combobox null ou tf proces etc
		// verificar se foi preenchido o campo das coordenadas //
		else if (dpDataPublicacao == null) { // ver de aceitar somente número 
			
			Alerta a = new Alerta ();
			a.alertar(new Alert(Alert.AlertType.ERROR, "Coordenadas inválidas!!!", ButtonType.OK));
			
			} 
		
					else {
				
						if (tipoInterferenciaID == 2) {
							
										if (tabSubCon.obterSubterranea().getSubTipoPocoFK() == null ||
											tabSubCon.obterSubterranea().getSubCaesb() == null ||
												tabSubCon.obterSubterranea().getSubSubSistemaFK() == null
											) {
										
										Alerta a = new Alerta ();
										a.alertar(new Alert(Alert.AlertType.ERROR, "Informe: Tipo de Captação (), Área é atendida pela Caesb() e Subsistema()!!!", ButtonType.OK));
										
										} else {
									
										// obter interferencia selecionada	
										Subterranea sub = tabSubCon.obterSubterranea();
										
										System.out.println("btn editar subterranea id " + tabSubCon.obterSubterranea().getInterID());
											
											sub.setInterTipoInterferenciaFK(tipoInterferencia);
											sub.setInterBaciaFK(baciaHid);
											sub.setInterUHFK(UniHid);
											sub.setInterTipoOutorgaFK(tipoOutorga);
											sub.setInterTipoAtoFK(tipoAto);
											sub.setInterSituacaoProcessoFK(situacaoProcesso);
										
											//sub.setInterDDLatitude(Double.parseDouble(tfLat.getText()));
											//sub.setInterDDLongitude(Double.parseDouble(tfLon.getText()));
											
											if (dpDataPublicacao.getValue() == null) {
												sub.setInterDataPublicacao(null);;}
												else {
													sub.setInterDataPublicacao(Date.valueOf(dpDataPublicacao.getValue()));
													
													}
															
											if (dpDataVencimento.getValue() == null) {
												sub.setInterDataVencimento(null);}
												else {
													sub.setInterDataVencimento(Date.valueOf(dpDataVencimento.getValue()));
												
													}
											
											sub.setInterNumeroAto(tfNumeroAto.getText());
											
											sub.setInterProcRenovacao(tfProcesoOutorga.getText());
											sub.setInterDespachoRenovacao(tfDespachoOutorga.getText());
											
											sub.setIntAtualizacao(Timestamp.valueOf((LocalDateTime.now())));
											
											sub.setInterEnderecoFK(endereco);
												
											/*
												GeometryFactory geoFac = new GeometryFactory();
												
												Point p = geoFac.createPoint(new Coordinate(
														Double.parseDouble(tfLon.getText()),
														Double.parseDouble(tfLat.getText()
														)));
												
												p.setSRID(4674);
													
												sub.setInterGeom(p);
												*/
											
											InterferenciaDao interferenciaDao = new InterferenciaDao ();
													
											// merge subterranea //
											interferenciaDao.mergeInterferencia(sub);
								
											obsList.remove(sub);
											obsList.add(sub);
											
										modularBotoes ();
										
										//-- Alerta de endereco salvo --//
										Alerta a = new Alerta ();
										a.alertar(new Alert(Alert.AlertType.INFORMATION, "Interferência editada com sucesso!", ButtonType.OK));
										
										}
									
								} // fim subterranea
						
						
						else if (tipoInterferenciaID == 1) {
							
								if (tabSupCon.obterSuperficial().getSupLocalCaptacaoFK() == null // || 
									//	tabSupCon.obterSuperficial().getSupArea() == null
										
										) {
									
									Alerta a = new Alerta ();
									a.alertar(new Alert(Alert.AlertType.ERROR, "Informe o Local de Captação e se há Caesb!!!", ButtonType.OK));
									
									} else {
										
												Superficial sup = tabSupCon.obterSuperficial();
												
												System.out.println("btn editar - super valor id " + tabSupCon.obterSuperficial().getInterID());
												
												sup.setInterTipoInterferenciaFK(tipoInterferencia);
												sup.setInterBaciaFK(baciaHid);
												sup.setInterUHFK(UniHid);
												sup.setInterTipoOutorgaFK(tipoOutorga);
												sup.setInterTipoAtoFK(tipoAto);
												sup.setInterSituacaoProcessoFK(situacaoProcesso);
												
												//sup.setInterDDLatitude(Double.parseDouble(tfLat.getText()));
												//sup.setInterDDLongitude(Double.parseDouble(tfLon.getText()));
												
												if (dpDataPublicacao.getValue() == null) {
													sup.setInterDataPublicacao(null);;}
													else {
														sup.setInterDataPublicacao(Date.valueOf(dpDataPublicacao.getValue()));
														
														}
																
												if (dpDataVencimento.getValue() == null) {
													sup.setInterDataVencimento(null);}
													else {
														sup.setInterDataVencimento(Date.valueOf(dpDataVencimento.getValue()));
													
														}
												
												sup.setInterNumeroAto(tfNumeroAto.getText());
												
												sup.setInterProcRenovacao(tfProcesoOutorga.getText());
												sup.setInterDespachoRenovacao(tfDespachoOutorga.getText());
												
												sup.setIntAtualizacao(Timestamp.valueOf((LocalDateTime.now())));
											
												sup.setInterEnderecoFK(endereco);
												
												/*
												GeometryFactory geoFac = new GeometryFactory();
												
												Point p = geoFac.createPoint(new Coordinate(
														Double.parseDouble(tfLon.getText()),
														Double.parseDouble(tfLat.getText()
														)));
												
												p.setSRID(4674);
												
												sup.setInterGeom(p);
												*/
											
											InterferenciaDao interferenciaDao = new InterferenciaDao ();
											
											// merge superficial e canal //
											interferenciaDao.mergeInterferencia(sup);
											
											//interferencia.setIntSupFK(sup);
												
											obsList.remove(sup);
											obsList.add(sup);
											
											modularBotoes ();
											
											//-- Alerta de endereco salvo --//
											Alert a = new Alert (Alert.AlertType.INFORMATION);
											a.setTitle("Parabéns!");
											a.setContentText("Interferência editada com sucesso!");
											a.setHeaderText(null);
											a.show();
											
											}
											
												
									} // fim superficial //
						
						else {
							
							Interferencia inter= tvLista.getSelectionModel().getSelectedItem();
							
							inter.setInterTipoInterferenciaFK(tipoInterferencia);
							inter.setInterBaciaFK(baciaHid);
							inter.setInterUHFK(UniHid);
							inter.setInterTipoOutorgaFK(tipoOutorga);
							inter.setInterTipoAtoFK(tipoAto);
							inter.setInterSituacaoProcessoFK(situacaoProcesso);
					
							//inter.setInterDDLatitude(Double.parseDouble(tfLat.getText()));
							//inter.setInterDDLongitude(Double.parseDouble(tfLon.getText()));
							
							if (dpDataPublicacao.getValue() == null) {
								inter.setInterDataPublicacao(null);;}
								else {
									inter.setInterDataPublicacao(Date.valueOf(dpDataPublicacao.getValue()));
									
									}
											
							if (dpDataVencimento.getValue() == null) {
								inter.setInterDataVencimento(null);}
								else {
									inter.setInterDataVencimento(Date.valueOf(dpDataVencimento.getValue()));
								
									}
							
							inter.setInterNumeroAto(tfNumeroAto.getText());
							
							inter.setInterProcRenovacao(tfProcesoOutorga.getText());
							inter.setInterDespachoRenovacao(tfDespachoOutorga.getText());
								
							inter.setIntAtualizacao(Timestamp.valueOf((LocalDateTime.now())));
							
							inter.setInterEnderecoFK(endereco);
								
							/*
								GeometryFactory geoFac = new GeometryFactory();
								
								Point p = geoFac.createPoint(new Coordinate(
										Double.parseDouble(tfLon.getText()),
										Double.parseDouble(tfLat.getText()
										)));
								
								p.setSRID(4674);
								
								inter.setInterGeom(p);
								*/
								
								/*
								GeometryFactory geoFac = new GeometryFactory();
								
								interferencia.setInterGeoLatLon(geoFac.createPoint(new Coordinate(
										Double.parseDouble(tfLon.getText()),
										Double.parseDouble(tfLat.getText())
										)));
										*/
								
								InterferenciaDao interferenciaDao = new InterferenciaDao ();
								
								//merge outras interferencias //
								interferenciaDao.mergeInterferencia(inter);
								
									obsList.remove(inter);
									obsList.add(inter);
									
									modularBotoes ();
									
										//-- Alerta de endereco salvo --//
										Alerta a = new Alerta ();
										a.alertar(new Alert(Alert.AlertType.INFORMATION, "Interferência editada com sucesso!", ButtonType.OK));
							
							
						} // fim outras interferencias
						
				}
		
		}
		
	public void btnExcluirHab () {
		
		try {
			
			Interferencia inter = tvLista.getSelectionModel().getSelectedItem();
			
			InterferenciaDao interferenciaDao = new InterferenciaDao ();
			
			interferenciaDao.removeInterferencia(inter.getInterID());
			
			// remover a interferencia da lista //
			obsList.remove(inter);
			
			modularBotoes ();
			
				Alerta a = new Alerta ();
				a.alertar(new Alert(Alert.AlertType.INFORMATION, "Cadastro excluído!", ButtonType.OK));
				
		}
		
		catch (Exception e) {
			
				Alerta a = new Alerta ();
				a.alertar(new Alert(Alert.AlertType.ERROR, "Erro ao excluir o cadastro!!!", ButtonType.OK));
		}
		
	}
	
	Pane pInterTipo = new Pane();
	
	@FXML Pane pInterferencia;
	
	Pane p1 = new Pane();
	BorderPane bp1 = new BorderPane();
	BorderPane bp2 = new BorderPane();
	ScrollPane sp = new ScrollPane();
	Pane pMapa = new Pane();
	  
	/* array de posicoes prefWidth prefHeight Layout Y e X */
	Double prefSizeWHeLayXY [][];
		
	Componentes com;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		 bp1.minWidthProperty().bind(pInterferencia.widthProperty());
		    bp1.maxHeightProperty().bind(pInterferencia.heightProperty().subtract(60));
		    
		    bp1.getStyleClass().add("border-pane");
		    
		    bp2.setPrefHeight(800.0);
		    bp2.minWidthProperty().bind(pInterferencia.widthProperty());
		    
		    sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		    
		    sp.setContent(bp2);
		    
		    bp1.setCenter(sp);
		    
		    pInterferencia.getChildren().add(bp1);
		    
		    p1.setMaxSize(1030.0, 1200.0);
		    p1.setMinSize(1030.0, 1200.0);
		    
		    bp2.setTop(p1);
		    BorderPane.setAlignment(p1, Pos.CENTER);
		    
		inicializarComponentes ();
		    
		tcLogradouro.setPrefWidth(445);
	    tcTipoInterferencia.setPrefWidth(230);
	    tcSituacao.setPrefWidth(230);
		
	    tcTipoInterferencia.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Interferencia, String>, ObservableValue<String>>() {
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Interferencia, String> i) {
		    	return new SimpleStringProperty(i.getValue().getInterTipoInterferenciaFK().getTipoInterDescricao());
		       
		    }
		});
	    
	    tcLogradouro.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Interferencia, String>, ObservableValue<String>>() {
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Interferencia, String> i) {
		    	return new SimpleStringProperty(i.getValue().getInterEnderecoFK().getEndLogradouro());
		       
		    }
		});
	    
	    tcSituacao.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Interferencia, String>, ObservableValue<String>>() {
		    public ObservableValue<String> call(TableColumn.CellDataFeatures<Interferencia, String> i) {
		    	return new SimpleStringProperty(i.getValue().getInterSituacaoProcessoFK().getSituacaoProcessoDescricao());
		       
		    }
		});
	    
	    tvLista.getColumns().add(tcTipoInterferencia);
	    	tvLista.getColumns().add(tcLogradouro);
	    		tvLista.getColumns().add(tcSituacao);
	
	    			tvLista.setItems(obsList);
	    
	    tvLista.setPrefSize(930, 185);
		tvLista.setLayoutX(50);
		tvLista.setLayoutY(335);
	    
		lblDataAtualizacao.setPrefSize(247, 22);
	    lblDataAtualizacao.setLayoutX(730);
	    lblDataAtualizacao.setLayoutY(530);
	 
	    obterEndereco ();
	  
	    // para trazer os dados do tipo de interferencia especifico //
	    pInterTipo.setPrefSize(960, 410);
	    pInterTipo.setLayoutX(90);
	    pInterTipo.setLayoutY(580);
	    
		p1.getChildren().addAll(lblDataAtualizacao, tvLista);
		
		olTipoInterferencia = FXCollections.observableArrayList(
				
				"Superficial",
				"Subterrânea" ,
				"Canal",
				"Caminhão Pipa",
				"Lançamento de Águas Pluviais",
				"Lançamento de Efluentes",
				"Barragem"
				
				);
		
		/*
		olBacia = FXCollections
				.observableArrayList(
						
						"Rio Corumbá"	,
						"Rio Descoberto"	,
						"Rio Paranã"	,
						"Rio São Bartolomeu"	,
						"Rio São Marcos"	,
						"Rio Maranhão"	,
						"Rio Paranoá"	,
						"Rio Preto"	

						); 
		
		olUniHid = FXCollections
				.observableArrayList(
						
						"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20",
						"21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41"
						); 
		
		
		*/
		olTipoOutorga = FXCollections
				.observableArrayList(
						
						"Outorga",
						"Renovação de Outorga",
						"Transferência de Outorga",
						"Revogação de Outorga",
						"Suspensão de Outorga",
						"Outorga Prévia",
						"Renovação de Outorga Prévia",
						"Transferência de Outorga Prévia",
						"Revogação de Outorga Prévia",
						"Suspensão de Outorga Prévia",
						"Registro",
						"Transferência de Registro",
						"Revogação de Registro",
						"Suspensão de Registro"	

						); 
		
		olTipoAto = FXCollections
				.observableArrayList(
						
						"Despacho"	,
						"Portaria"	,
						"Registro"	,
						"Resolução",
						"Resolução ANA",
						"Portaria DNAEE"
						
						); 
		
		olSituacao = FXCollections
				.observableArrayList(
						
						"Arquivado",
						"Em Análise",
						"Outorgado",
						"Vencida",
						"Arquivado (CNRH 16)",
						"Pendência"	,
						"Indeferido",
						"Revogado"
		); 
		
		cbTipoInterferencia.setItems(olTipoInterferencia);
		//cbBacia.setItems(olBacia);
		//cbUnidHid.setItems(olUniHid);
		cbTipoOutorga.setItems(olTipoOutorga);
		
		cbTipoOutorga.setCellFactory(
	            new Callback<ListView<String>, ListCell<String>>() {
	               
	            	@Override public ListCell<String> call(ListView<String> param) {
	            		
	                    final ListCell<String> cell = new ListCell<String>() {
	                        {
	                            super.setPrefWidth(100);
	                        }    
                    @Override public void updateItem(String item, 
                        boolean empty) {
                            super.updateItem(item, empty);
                            if (item != null) {
                                setText(item);    
                                if (		item.equals("Outorga") 
                                		|| 	item.equals("Renovação de Outorga") 
                                		|| 	item.equals("Transferência de Outorga") 
                                		|| 	item.equals("Revogação de Outorga") 
                                		|| 	item.equals("Suspensão de Outorga")
                                		) {
                                	setTextFill(Color.BLUE);
                                }
                                
                                else if (	item.equals("Outorga Prévia") 
                                		|| 	item.equals("Renovação de Outorga Prévia") 
                                		|| 	item.equals("Transferência de Outorga Prévia") 
                                		|| 	item.equals("Revogação de Outorga Prévia") 
                                		|| 	item.equals("Suspensão de Outorga Prévia")
                                		) {
                                    setTextFill(Color.CHOCOLATE);
                                }
                                
                                else {
                                    setTextFill(Color.DIMGRAY);
                                }
                            }
                        } // fim metodo updateItem
	                };
	                
	                return cell;
	                
	            } // fim metodo call
	        });
		
		cbTipoAto.setItems(olTipoAto);
		cbSituacao.setItems(olSituacao);
		
		// capturar o id do tipo de interferencia //
		cbTipoInterferencia.getSelectionModel().selectedIndexProperty().addListener(new
	            ChangeListener<Number>() {
	    	public void changed(@SuppressWarnings("rawtypes") ObservableValue ov,
	    		Number value, Number new_value) {
	    		
	    		if ( (Integer) new_value !=  -1)
	    			tipoInterferenciaID = listaTipoInterID [(int) new_value];
	    			
	    		try {
					abrirTabs(tipoInterferenciaID);
				} catch (IOException e) {
					System.out.println("erro ao abrirTabs" + e);
					
				}
	    		
            }
	    });
	    
		cbTipoInterferencia.getSelectionModel().selectedItemProperty().addListener( 
				
	    	(ObservableValue<? extends String> observable, String oldValue, String newValue) ->
	    	
	    	tipoInterferenciaDescricao = (String) newValue
	     );
		
		/*
		cbBacia.getSelectionModel().selectedIndexProperty().addListener(new
	            ChangeListener<Number>() {
	    	public void changed(@SuppressWarnings("rawtypes") ObservableValue ov,
	    		Number value, Number new_value) {
	    		
	    		if ( (Integer) new_value !=  -1)
	    			
	    		baciaID = listaBaciasID [(int) new_value];
	    		
	    		System.out.println(" bacia id " + baciaID);
	    		
            }
	    });
	    */
		
		cbTipoOutorga.getSelectionModel().selectedIndexProperty().addListener(new
	            ChangeListener<Number>() {
	    	public void changed(@SuppressWarnings("rawtypes") ObservableValue ov,
	    		Number value, Number new_value) {
	    		
	    		if ( (Integer) new_value !=  -1)
	    			tipoOutorgaID = listaTipoOutorgaID [(int) new_value];
	    			System.out.println("tipo outorga " + tipoOutorgaID);
	    			
            }
	    });
		
		cbTipoAto.getSelectionModel().selectedIndexProperty().addListener(new
	            ChangeListener<Number>() {
	    	public void changed(@SuppressWarnings("rawtypes") ObservableValue ov,
	    		Number value, Number new_value) {
	    		
	    		if ( (Integer) new_value !=  -1)
	    			tipoAtoID = listaTipoAtoID [(int) new_value];
	    			System.out.println("situação " + tipoAtoID);
	    			
            }
	    });
		
		cbSituacao.getSelectionModel().selectedIndexProperty().addListener(new
	            ChangeListener<Number>() {
	    	public void changed(@SuppressWarnings("rawtypes") ObservableValue ov,
	    		Number value, Number new_value) {
	    		
	    		if ( (Integer) new_value !=  -1)
	    			situacaoProcessoID = listaSituacaoProcessoID [(int) new_value];
	    			System.out.println("situação " + situacaoProcessoID);
	    			
            }
	    });
	    
		/*
		cbBacia.getSelectionModel()
	    	.selectedItemProperty()
	    	.addListener( 
	    	(ObservableValue<? extends String> observable, String oldValue, String newValue) ->
	    	
	    	baciaNome = (String) newValue
	    	
	    );
	    */
		
		/*
		cbUnidHid.getSelectionModel().selectedIndexProperty().addListener(new
	            ChangeListener<Number>() {
	    	public void changed(@SuppressWarnings("rawtypes") ObservableValue ov,
	    		Number value, Number new_value) {
	    		
	    		if ( (Integer) new_value !=  -1)
	    		unidHidID = listaUHID [(int) new_value];
	    		
	    		System.out.println("unidade hidr selecionada " + unidHidID);
	    		
            }
	    });
	    */
		
		modularBotoes ();
		selecionarInterferencia();
		
		 btnNovo.setOnAction(new EventHandler<ActionEvent>() {

		        @Override
		        public void handle(ActionEvent event) {
		            btnNovoHab();
		        }
		    });
			    
		    btnSalvar.setOnAction(new EventHandler<ActionEvent>() {

		        @Override
		        public void handle(ActionEvent event) {
		            btnSalvarHab();
		        }
		    });
		    
		    btnEditar.setOnAction(new EventHandler<ActionEvent>() {

		        @Override
		        public void handle(ActionEvent event) {
		            btnEditarHab();
		        }
		    });
		    
		    btnCancelar.setOnAction(new EventHandler<ActionEvent>() {

		        @Override
		        public void handle(ActionEvent event) {
		            btnCancelarHab();
		        }
		    });
		    
		    btnPesquisar.setOnAction(new EventHandler<ActionEvent>() {

		        @Override
		        public void handle(ActionEvent event) {
		            //btnPesquisarHab();
		        }
		    });
		    
		    
		    /*
		    btnCapturarCoord.setOnAction(new EventHandler<ActionEvent>() {

		        @Override
		        public void handle(ActionEvent event) {
		        	 btnIntMapsHab(); 
		        }
		    });
		    */
		    
		    btnExcluir.setOnAction(new EventHandler<ActionEvent>() {

		        @Override
		        public void handle(ActionEvent event) {
		        	btnExcluirHab();
		        }
		    });
		    
	} // FIM INITIALIZE 
	
	ObservableList<String> olTipoInterferencia;
	ObservableList<String> olTipoOutorga;
	ObservableList<String> olSituacao;
	ObservableList<String> olTipoAto;

	Pane pEndereco;
	
		Label lblEndereco;
		Button btnEndereco;
	
		ArrayList<Node> listComponentesEndereco = new ArrayList<Node>();
			
	Pane pDadosInterferencia;

		ComboBox<String> cbTipoInterferencia;
		ComboBox<String> cbTipoOutorga;
		ComboBox<String> cbSubtipoOutorga;
		ComboBox<String> cbSubTipoOutorga;
		ComboBox<String> cbTipoAto;
		ComboBox<String> cbSituacao;
		
		ArrayList<Node> listComponentesInterferencia = new ArrayList<Node>();
		
		Pane pDI1;
		DatePicker dpDataPublicacao;
		DatePicker dpDataVencimento;
		TextField tfNumeroAto;
		
		ArrayList<Node> listComponentesInterferenciaInterno1 = new ArrayList<Node>();
		
		Pane pDI2;
		TextField tfProcesoOutorga;
		TextField tfDespachoOutorga;
			
		ArrayList<Node> listComponentesInterferenciaInterno2 = new ArrayList<Node>();
		
		
		Pane pPersistencia;
		  	Button btnNovo;
		  	Button btnSalvar;
		  	Button btnEditar;
		  	Button btnExcluir;
		  	Button btnCancelar;
		  	Button btnPesquisar;
		  	
		  	TextField tfPesquisar;
		  							
		  	ArrayList<Node> listComponentesPersistencia = new ArrayList<Node>();
		  	
		Pane pTipoInterferencia;  	
	  								
										
			
	public void inicializarComponentes () {
		
	   listComponentesEndereco.add(pEndereco = new Pane());
		   listComponentesEndereco.add(new Label("ENDERECO:"));
		   listComponentesEndereco.add(lblEndereco = new Label());
		   listComponentesEndereco.add(btnEndereco = new Button("<<<"));
		    
		    prefSizeWHeLayXY = new Double [][] { 
		    	{950.0,60.0,40.0,10.0},
		    	{740.0,30.0,105.0,15.0},
		    	{70.0,30.0,30.0,15.0},
		    	{65.0,25.0,855.0,18.0},
	    	};
		    	
			    com = new Componentes();
			    com.popularTela(listComponentesEndereco, prefSizeWHeLayXY, p1);
				    
		listComponentesInterferencia.add(pDadosInterferencia = new Pane());		    
			listComponentesInterferencia.add(new Label ("Tipo de Interferência:"));
			listComponentesInterferencia.add(cbTipoInterferencia = new ComboBox<>());
			listComponentesInterferencia.add(new Label ("Tipo de Outorga:"));
			listComponentesInterferencia.add(cbTipoOutorga = new ComboBox<>());
			listComponentesInterferencia.add(new Label ("Subtipo de Outorga:"));
			listComponentesInterferencia.add(cbSubTipoOutorga = new ComboBox<>());
			listComponentesInterferencia.add(new Label ("Tipo de Ato:"));
			listComponentesInterferencia.add(cbTipoAto = new ComboBox<>());
			listComponentesInterferencia.add(new Label ("Situação:"));
			listComponentesInterferencia.add(cbSituacao = new ComboBox<>());
		
		
			 prefSizeWHeLayXY = new Double [][] { 
				 {930.0,165.0,50.0,85.0},
				 {160.0,30.0,65.0,6.0},
				 {160.0,30.0,65.0,36.0},
				 {150.0,30.0,235.0,6.0},
				 {150.0,30.0,235.0,36.0},
				 {150.0,30.0,395.0,5.0},
				 {150.0,30.0,395.0,37.0},
				 {150.0,30.0,555.0,6.0},
				 {150.0,30.0,555.0,36.0},
				 {150.0,30.0,715.0,5.0},
				 {150.0,30.0,715.0,35.0},
		    	};
			    	
				    com = new Componentes();
				    com.popularTela(listComponentesInterferencia, prefSizeWHeLayXY, p1);
			    
		
		
	    listComponentesInterferenciaInterno1.add(pDI1 = new Pane());	
		    listComponentesInterferenciaInterno1.add(new Label ("Data de Publicação:"));
		    listComponentesInterferenciaInterno1.add(dpDataPublicacao = new DatePicker());
		    listComponentesInterferenciaInterno1.add(new Label ("Data de Vencimento:"));
		    listComponentesInterferenciaInterno1.add(dpDataVencimento = new DatePicker());
		    listComponentesInterferenciaInterno1.add(new Label ("Número do Ato:"));
		    listComponentesInterferenciaInterno1.add(tfNumeroAto = new TextField());
			
			 prefSizeWHeLayXY = new Double [][] { 
				 {400.0,80.0,120.0,79.0},
				 {130.0,30.0,10.0,10.0},
				 {130.0,30.0,10.0,40.0},
				 {130.0,30.0,150.0,10.0},
				 {130.0,30.0,150.0,40.0},
				 {100.0,30.0,290.0,10.0},
				 {100.0,30.0,290.0,40.0},
		    	};
			    	
				    com = new Componentes();
				    com.popularTela(listComponentesInterferenciaInterno1, prefSizeWHeLayXY, pDadosInterferencia);
			
		
	    listComponentesInterferenciaInterno2.add(pDI1 = new Pane());	
		    listComponentesInterferenciaInterno2.add(new Label ("Processo de Outorga:"));
		    listComponentesInterferenciaInterno2.add(tfProcesoOutorga = new TextField());
		    listComponentesInterferenciaInterno2.add(new Label ("Depacho de outorga:"));
		    listComponentesInterferenciaInterno2.add(tfDespachoOutorga = new TextField());
				
			 prefSizeWHeLayXY = new Double [][] { 
				 {290.0,80.0,530.0,79.0},
				 {130.0,30.0,10.0,10.0},
				 {130.0,30.0,10.0,40.0},
				 {130.0,30.0,150.0,10.0},
				 {130.0,30.0,150.0,40.0},
		    	};
			    	
				    com = new Componentes();
				    com.popularTela(listComponentesInterferenciaInterno2, prefSizeWHeLayXY, pDadosInterferencia);
		
		
				    
	    listComponentesPersistencia.add(pPersistencia = new Pane());
		    listComponentesPersistencia.add(btnNovo = new Button("NOVO"));
		    listComponentesPersistencia.add(btnSalvar = new Button("SALVAR"));
		    listComponentesPersistencia.add(btnEditar = new Button("EDITAR"));
		    listComponentesPersistencia.add(btnExcluir = new Button("EXCLUIR"));
		    listComponentesPersistencia.add(btnCancelar = new Button("CANCELAR"));
		    listComponentesPersistencia.add(tfPesquisar = new TextField());
		    listComponentesPersistencia.add(btnPesquisar = new Button("PESQUISAR"));
				    
		    prefSizeWHeLayXY = new Double[][] { 
		    	{930.0,60.0,50.0,260.0},
		    	{95.0,25.0,18.0,18.0},
		    	{95.0,25.0,123.0,18.0},
		    	{95.0,25.0,228.0,18.0},
		    	{95.0,25.0,333.0,18.0},
		    	{95.0,25.0,438.0,18.0},
		    	{265.0,25.0,543.0,18.0},
		    	{95.0,25.0,818.0,18.0},
		    };
								    				
			 com = new Componentes();
			    com.popularTela(listComponentesPersistencia, prefSizeWHeLayXY, p1);    
			    
		pTipoInterferencia = new Pane();
		
		pTipoInterferencia.setPrefSize(1030, 640);
		pTipoInterferencia.setLayoutX(0.0);
		pTipoInterferencia.setLayoutY(558.0);
		pTipoInterferencia.setStyle("-fx-background-color: red;");
		
		p1.getChildren().add(pTipoInterferencia);
		
		
		    
		
	}
	
	/*
	 
	OUTORGA SUBTERRÂNEA
	OUTORGA SUBTERRÂNEA TRANSFERÊNCIA
	OUTORGA SUBTERRÂNEA INDEFERIMENTO
	OUTORGA SUBTERRÂNEA MODIFICAÇÃO
	OUTORGA SUBTERRÂNEA RENOVAÇÃO
	REGISTRO SUBTERRÂNEA
	REGISTRO SUBTERRÂNEA MODIFICAÇÃO
	REGISTRO SUBTERRÂNEA TRANSFERÊNCIA
	OUTORGA SUBTERRÂNEA PRÉVIA 
	OUTORGA SUBTERRÂNEA PRÉVIA INDEFERIMENTO

	 */
	
	public void obterEndereco () {
		
		//p_lblEndereco.setPrefSize(900, 50);
		//p_lblEndereco.setLayoutX(120);
		//p_lblEndereco.setLayoutY(20);
		//p_lblEndereco.setStyle("-fx-background-color: #E9E9E9;");
		
		Label lblEnd = new Label ("Endereco: ");
		lblEnd.setLayoutX(25);
		lblEnd.setLayoutY(16);
		
		 // Label para preencher com o endereco a ser trabalhada //
	    lblEndereco.setStyle("-fx-font-weight: bold;");
	    lblEndereco.setPrefSize(750, 25);	
	    lblEndereco.setLayoutX(89);
	    lblEndereco.setLayoutY(13);
	    
	    btnBuscarEnd.setPrefSize(25, 25);
	    btnBuscarEnd.setLayoutX(851);
	    btnBuscarEnd.setLayoutY(13);
		
		//p_lblEndereco.getChildren().addAll(lblEnd, lblEndereco, btnBuscarEnd);
	}
	

	public void btnCancelarHab () {
		
			modularBotoes ();
			
			cbTipoInterferencia.getSelectionModel().clearSelection();
			
			pInterTipo.getChildren().clear();
		
		
	}
	
	
	
	public void abrirTabs (int ti) throws IOException {
		
		if (ti == 1) {
			
			pInterTipo.getChildren().clear();
			Pane tabSupPane = new Pane();
			tabSupCon = new TabSuperficialController();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/principal/TabSuperficial.fxml")); 
			loader.setRoot(tabSupPane);
			loader.setController(tabSupCon);
			loader.load();
			
			pInterTipo.getChildren().add(tabSupPane);
			
		}
		
		else if (ti == 2) {
			
			pInterTipo.getChildren().clear();
			Pane tabSubPane = new Pane();
			tabSubCon = new TabSubterraneaController();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/principal/TabSubterranea.fxml")); 
			loader.setRoot(tabSubPane);
			loader.setController(tabSubCon);
			loader.load();
			pInterTipo.getChildren().add(tabSubPane);
			
			}
		
			else {
				
				pInterTipo.getChildren().clear();
				
				}
		
	}
	
	// --- método para listar interferencias --- //
	public void listarInterferencias (String strPesquisa) {
	 	
		 	// --- conexao - listar enderecos --- //
			InterferenciaDao interferenciaDao = new InterferenciaDao();
			List<Interferencia> interferenciaList = null;
			
			try {
				interferenciaList = interferenciaDao.listInterferencia(strPesquisa);
			} catch (Exception e) {
				System.out.println("erro ao listar as interferências!");
				e.printStackTrace();
			}
					
			if (!obsList.isEmpty()) {
				obsList.clear();
			}
			
			// preencher a observable lista para a table view //
			for (Interferencia i : interferenciaList) {
				
				i.getInterID();
				
				i.getInterTipoInterferenciaFK();
				i.getInterBaciaFK();
				i.getInterUHFK();
				i.getInterTipoOutorgaFK();
				i.getInterTipoAtoFK();
				
				i.getInterDDLatitude();
				i.getInterDDLongitude();
				
				i.getInterSituacaoProcessoFK();
				i.getInterDataPublicacao();
				i.getInterDataVencimento();
				i.getInterNumeroAto();
				
				obsList.add(i);
				
			}
			
			tvLista.setItems(obsList); 
			
	 }
	 	
	 	// metodo selecionar interferencia -- //
	public void selecionarInterferencia () {
		
	 		tvLista.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
				
				public void changed(ObservableValue<?> observable , Object oldValue, Object newValue) {
				
				Interferencia inter = (Interferencia) newValue;
				
				if (inter == null) {
					
					btnNovo.setDisable(true);
					btnSalvar.setDisable(true);
					
					btnEditar.setDisable(false);
					btnExcluir.setDisable(false);
					btnCancelar.setDisable(false);
					
				} else {
					
					cbTipoInterferencia.setValue(inter.getInterTipoInterferenciaFK().getTipoInterDescricao());
					
					cbTipoOutorga.setValue(inter.getInterTipoOutorgaFK().getTipoOutorgaDescricao());
					//cbSubTipoOutorga.setValue(inter.getInterTipoOutorgaFK().getTipoOutorgaDescricao());
					cbTipoAto.setValue(inter.getInterTipoAtoFK().getTipoAtoDescricao());
					cbSituacao.setValue(inter.getInterSituacaoProcessoFK().getSituacaoProcessoDescricao());
					
					Date dPub = inter.getInterDataPublicacao();
					dpDataPublicacao.setValue(dPub.toLocalDate());
					
					Date dVen = inter.getInterDataVencimento();
					dpDataVencimento.setValue(dVen.toLocalDate());
					
					tfNumeroAto.setText(inter.getInterNumeroAto());
					tfProcesoOutorga.setText(inter.getInterProcRenovacao());
					tfDespachoOutorga.setText(inter.getInterDespachoRenovacao());
					
					// mostrar data de atualizacao //
					FormatoData d = new FormatoData();
					try {lblDataAtualizacao.setText("Data de Atualização: " + d.formatarData(inter.getIntAtualizacao()));
							lblDataAtualizacao.setTextFill(Color.BLACK);
							System.out.println("teste data atualizacao black");
					}catch (Exception e) {
							lblDataAtualizacao.setText("Não há data de atualização!");
							lblDataAtualizacao.setTextFill(Color.RED);
							System.out.println("teste data atualizacao red");
					}
				
					setEndereco(inter.getInterEnderecoFK());
					
					tipoInterferenciaID = inter.getInterTipoInterferenciaFK().getTipoInterID();
					
					if (tipoInterferenciaID == 2) {
						
						try {
							abrirTabs (tipoInterferenciaID);
						} catch (IOException e) {
							
							e.printStackTrace();
						}
						
						tabSubCon.imprimirSubterranea(((Subterranea) inter));
						
					}
					
					if (tipoInterferenciaID == 1 || tipoInterferenciaID == 3) {
						
						try {
							abrirTabs (tipoInterferenciaID);
						} catch (IOException e) {
							
							e.printStackTrace();
						}
						
						tabSupCon.imprimirSuperficial(((Superficial) inter));
						
					}
					
					//System.out.println("FK " + intTab.getEnderecoInterferenciaObjetoTabelaFK());
					
					//Double lat = Double.parseDouble(tfLat.getText());
					//Double  lng = Double.parseDouble(tfLon.getText() );
					
					/*
					if (wv1 != null) {
						
						String endCoord = "" + intTab.getEnderecoInterferenciaObjetoTabelaFK().getLat_Endereco() + ","
										+ intTab.getEnderecoInterferenciaObjetoTabelaFK().getLon_Endereco();
						
						String intCoord = "" + intTab.getInter_Lat() +  "," + intTab.getInter_Lng();
						
						//System.out.println(endCoord);
						//System.out.println(intCoord);
						
						webEng.executeScript("" +
		                        "window.coordenadas = [" 
		                        + 	"'" + endCoord + "'," 
		                        +	"'" + intCoord + "'" 
		                        + "];"
		                        + "document.buscarCoordenadas(coordenadas);"
		                        + " map.setCenter({lat: " 
		                        + intTab.getEnderecoInterferenciaObjetoTabelaFK().getLat_Endereco()
		                        + ", lng: "  
		                        + intTab.getEnderecoInterferenciaObjetoTabelaFK().getLon_Endereco()
		                        + "});"
		                    );
		                    */
		                    
						
						/*
						webEng.executeScript("" +
		                        "window.lat = " + lat + ";" +
		                        "window.lon = " + lng + ";" +
		                        "document.goToLocation(window.lat, window.lon);"
		                    );
		                    */
						
					}
					
					btnNovo.setDisable(true);
					btnSalvar.setDisable(true);
					btnEditar.setDisable(false);
					btnExcluir.setDisable(false);
					btnCancelar.setDisable(false);
					
					}
			});
		}
	 	
	public void modularBotoes () {
		
 		cbTipoInterferencia.setDisable(true);
		
		cbTipoOutorga.setDisable(true);
		cbSubTipoOutorga.setDisable(true);
		cbTipoAto.setDisable(true);
		cbSituacao.setDisable(true);
		
		dpDataPublicacao.setDisable(true);
		dpDataVencimento.setDisable(true);
		tfNumeroAto.setDisable(true);
		tfProcesoOutorga.setDisable(true);
		tfDespachoOutorga.setDisable(true);
		
		btnSalvar.setDisable(true);
		btnEditar.setDisable(true);
		btnExcluir.setDisable(true);
		
		btnNovo.setDisable(false);
	}
	

}
