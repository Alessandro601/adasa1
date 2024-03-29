package controladores.principal;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;

public class ControladorFiscalizacao {
	
	@FXML Pane pFiscalizacao;
	@FXML TabPane tpFiscalizacao = new TabPane();
	
	@FXML Tab tpTabDemanda;
		String strDem = "/fxml/principal/TabDemanda.fxml";
	
		@FXML Tab tpTabEndereco;
			String strEnd = "/fxml/principal/TabEndereco.fxml";
	
			@FXML Tab tpTabInterferencia;
			String strInt = "/fxml/principal/TabInterferencia.fxml";
	
				@FXML Tab tpTabUsuario;
				String strUs = "/fxml/principal/TabUsuario.fxml";
	
					@FXML Tab tpTabVistoria;
					String strVis = "/fxml/fiscalizacao/TabVistoria.fxml";
	
						@FXML Tab tpTabAto;
						String strAto = "/fxml/fiscalizacao/TabAto.fxml";
	
	
	@FXML 
    private void initialize() {
		
		tpFiscalizacao.prefWidthProperty().bind(pFiscalizacao.widthProperty());
		tpFiscalizacao.prefHeightProperty().bind(pFiscalizacao.heightProperty());
		
		abrirTab (new Pane() , new TabDemandaControlador(1), strDem, tpTabDemanda );
			abrirTab (new Pane() ,  new TabEnderecoControlador(1), strEnd, tpTabEndereco );
				abrirTab (new Pane() ,  new TabInterferenciaControlador(1), strInt, tpTabInterferencia );
					abrirTab (new Pane() ,  new TabUsuarioControlador(1), strUs, tpTabUsuario );
						abrirTab (new Pane() ,  new TabVistoriaControlador(), strVis, tpTabVistoria );
							abrirTab (new Pane(),  new TabAtoControlador(), strAto, tpTabAto );
							
							
		
	}
	
	public void abrirTab (Pane p , Object o, String strFXML, Tab t ) {
		
    	FXMLLoader loader = new FXMLLoader(getClass().getResource(strFXML));
		
		loader.setRoot(p);
		loader.setController(o);
		try {
			loader.load();
		} catch (IOException e) {
			System.out.println("erro na abertura do pane");
			e.printStackTrace();
		}
		
		p.minWidthProperty().bind(pFiscalizacao.widthProperty());
		p.minHeightProperty().bind(pFiscalizacao.heightProperty());
		
		t.setContent(p);
		
	}

}



/*
Pane pVistoria = new Pane();

tabVis = new TabVistoriaControlador();

FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/fiscalizacao/TabVistoria.fxml"));

loader.setRoot(pVistoria);
loader.setController(tabVis);
try {
	loader.load();
} catch (IOException e) {
	System.out.println("erro na abertura do pane vistoria");
	e.printStackTrace();
}

pVistoria.minWidthProperty().bind(pFiscalizacao.widthProperty());
pVistoria.minHeightProperty().bind(pFiscalizacao.heightProperty());

tpTabVistoria.setContent(pVistoria);
*/


/*
tpFiscalizacao.widthProperty().addListener(new ChangeListener<Number>() {
	   
    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
    	
    	//tabVistoria.redimWid (newValue);
    	
    	System.out.println("bind tpFisc " + newValue);
    	
    }
});
*/



/*
if (tabDemandaControlador == null) {
	
	tabDemandaControlador = new TabDemandaControlador();
}
*/

/*
pFiscalizacao.widthProperty().addListener(new ChangeListener<Number>() {
	   
    @Override 
    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
    	
    	
    	System.out.println("largura p fiscalizacao "  + pFiscalizacao.getWidth());
    	//tabDemandaControlador.redimWid (newValue);
    	
    }
});

pFiscalizacao.heightProperty().addListener(new ChangeListener<Number>() {
	   
    @Override 
    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
    	
    	
    	System.out.println("altura p fiscalizacao "  + pFiscalizacao.getHeight());
    	//tabDemandaControlador.redimHei (newValue);
    	
    }
});
*/


/*
p = new Pane();

tabDemandaControlador = new TabDemandaControlador();

FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/fiscalizacao/TabDemanda.fxml"));

//loader.setRoot(p);
loader.setController(tabDemandaControlador);

try {
	loader.load();
} catch (IOException e) {
	e.printStackTrace();
}
*/


/*
tabDemandaControlador = new TabDemandaControlador();

pFiscalizacao.widthProperty().addListener(new ChangeListener<Number>() {
	   
    @Override 
    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
    	
    	// Demanda //
    	tabDemandaControlador.redimensionarLargura(newValue);
    	
    }
});



pFiscalizacao.heightProperty().addListener(new ChangeListener<Number>() {
	   
    @Override 
    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
    	
    	// Demanda //
    	tabDemandaControlador.redimensionarAltura(newValue);
    	
    }
});
*/
