package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import entidades.HibernateUtil;
import entidades.Processo;

public class ProcessoDao {
	
  public void salvarProcesso(Processo pro) {
	  
    Session s = HibernateUtil.getSessionFactory().openSession();
    s.beginTransaction();
    s.save(pro);
    s.getTransaction().commit();
    s.flush();
    s.close();
    
  }
  
  @SuppressWarnings("unchecked")
  public List<Processo> listarProcessos(String strPesquisaProcesso) {
	  
    Session s = HibernateUtil.getSessionFactory().openSession();
    
    List<Processo> list = new ArrayList<Processo>();
    
    Criteria crit = s.createCriteria(Processo.class, "processo");
    
    crit.createAlias("processo.demandas", "demandas", JoinType.LEFT_OUTER_JOIN);
    crit.createAlias("demandas.demEnderecoFK", "endereco", JoinType.LEFT_OUTER_JOIN);
    crit.createAlias("endereco.endRAFK", "ra", JoinType.LEFT_OUTER_JOIN);
    
    Criterion proNumeroSEI = Restrictions.like("processo.proSEI", '%' + strPesquisaProcesso + '%');
    Criterion proInteressado = Restrictions.like("processo.proInteressado", '%' + strPesquisaProcesso + '%');
    Criterion proEndereco = Restrictions.like("endereco.endLogradouro", '%' + strPesquisaProcesso + '%');
    
    Disjunction orExp = Restrictions.or(new Criterion[] { proNumeroSEI, proInteressado, proEndereco });
    
    crit.add(orExp);
    
    list = crit.list();
    
    s.beginTransaction();
    
    s.getTransaction().commit();
    s.close();
    
    return list;
    
  }
  
  public void removerProcesso(Integer id) {
	  
    Session s = HibernateUtil.getSessionFactory().openSession();
    s.beginTransaction();
    Processo p = (Processo)s.load(Processo.class, id);
    s.delete(p);
    s.getTransaction().commit();
    s.close();
    
  }
  
  public void editarProcesso(Processo pro) {
	  
    Session s = HibernateUtil.getSessionFactory().openSession();
    s.beginTransaction();
    s.update(pro);
    s.getTransaction().commit();
    s.close();
    
  }
  
  public void mergeProcesso(Processo pro) {
	  
    Session s = HibernateUtil.getSessionFactory().openSession();
    s.beginTransaction();
    s.merge(pro);
    s.getTransaction().commit();
    s.close();
    
  }
  
  public void persistProcesso(Processo pro) {
	  
    Session s = HibernateUtil.getSessionFactory().openSession();
    s.beginTransaction();
    s.persist(pro);
    s.getTransaction().commit();
    s.close();
    
  }
  
}
