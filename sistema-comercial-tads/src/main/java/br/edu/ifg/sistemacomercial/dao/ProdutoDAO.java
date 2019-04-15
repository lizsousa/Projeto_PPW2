package br.edu.ifg.sistemacomercial.dao;

import br.edu.ifg.sistemacomercial.entity.Categoria;
import br.edu.ifg.sistemacomercial.entity.Produto;
import br.edu.ifg.sistemacomercial.util.FabricadeConexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO{

    public void salvar(Produto entity) throws SQLException{
        //Ordem das colunas: nome, marca, codigo de barras, unidade de medida, categoria
        String sqlInsert = "insert into produto (nome, marca, codigo_barras, unidade_medida, categoria_id, id"
                + ") values (?, ?, ?, ?, ?, default)";
        String sqlUpdate = "update produto set nome = ?, marca = ?, unidade_medida = ?, codigo_barras = ?, "
                + ", categoria_id = ? where id = ?";
        
        PreparedStatement  ps;
        if(entity.getId() == null){
            ps = FabricadeConexao.getConexao().prepareStatement(sqlInsert);
        } else {
            ps = FabricadeConexao.getConexao().prepareStatement(sqlUpdate);
            ps.setInt(6, entity.getId());
        }
        ps.setString(1, entity.getNome());
        ps.setString(2, entity.getMarca());
        ps.setString(3, entity.getCodigoBarras());
        ps.setString(4, entity.getUnidadeMedida());
        ps.setInt(5, entity.getCategoria().getId());
        ps.execute();
        FabricadeConexao.fecharConexao();
    }
    
    public void deletar(Produto entity) throws SQLException{
        String sqlDelete = "delete from produto where id = ?";
        PreparedStatement ps = FabricadeConexao.getConexao().prepareStatement(sqlDelete);
        ps.setInt(1, entity.getId());
        ps.execute();
        FabricadeConexao.fecharConexao();
    }
    
    public List<Produto> listar() throws SQLException{
        String sqlQuery = "select * from produto";
        PreparedStatement ps = FabricadeConexao.getConexao().prepareStatement(sqlQuery);
        
        //java.sql.ResultSet
        ResultSet rs = ps.executeQuery();
        List<Produto> produtos = new ArrayList<>();
        while(rs.next()){
            Produto produto = new Produto();
            produto.setId(rs.getInt("Id"));
            produto.setNome(rs.getString("nome"));
            produto.setMarca(rs.getString("marca"));
            produto.setCodigoBarras(rs.getString("codigo_barras"));
            produto.setUnidadeMedida(rs.getString("unidade_medida"));
            int idCategoria = rs.getInt("categoria_id");
            produto.setCategoria(buscaCategoria(idCategoria));
            produtos.add(produto);
        }
        FabricadeConexao.fecharConexao();
        return produtos;
    }
    
    public Categoria buscaCategoria(int id) throws SQLException{
        PreparedStatement psCat = FabricadeConexao.getConexao().prepareStatement("select * from categoria where id = ?");
        psCat.setInt(1, id);
        ResultSet rsCat = psCat.executeQuery();
        if(rsCat.next()){
            Categoria categoria = new Categoria();
            categoria.setId(id);
            categoria.setNome(rsCat.getString("nome"));
            return categoria;
        }
        return null;
    }
    
}
