package entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table (name="Subtipo_Outorga")
public class SubtipoOutorga implements Serializable {
	
	private static final long serialVersionUID = 531945803376118528L;


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column (name="so_ID")
	private int subtipoOutorgaID; 


    @Column (name="so_Descricao", columnDefinition="varchar(250)")
	private String subtipoOutorgaDescricao;
    
	    @OneToMany (mappedBy = "interSubtipoOutorgaFK", cascade = CascadeType.MERGE,
				fetch = FetchType.LAZY, targetEntity = Interferencia.class)
		@Fetch(FetchMode.SUBSELECT) 
		private List<Interferencia> interferencias = new ArrayList<Interferencia>();

		public int getSubtipoOutorgaID() {
			return subtipoOutorgaID;
		}

		public void setSubtipoOutorgaID(int subtipoOutorgaID) {
			this.subtipoOutorgaID = subtipoOutorgaID;
		}

		public String getSubtipoOutorgaDescricao() {
			return subtipoOutorgaDescricao;
		}

		public void setSubtipoOutorgaDescricao(String subtipoOutorgaDescricao) {
			this.subtipoOutorgaDescricao = subtipoOutorgaDescricao;
		}

		public List<Interferencia> getInterferencias() {
			return interferencias;
		}

		public void setInterferencias(List<Interferencia> interferencias) {
			this.interferencias = interferencias;
		}
	
	
	 

}
