package br.com.cursoandroid.brabrank.model;

import java.util.ArrayList;
import java.util.List;

import br.com.cursoandroid.brabrank.model.entity.Usuario;

public class UsuarioDAO {

    private static List<Usuario> usuarios = new ArrayList<>();

    public List<Usuario> listarTodos(){
        return usuarios;
    }

    public Usuario buscarPorEmail(String email){
        // Percorrer a lista com o for each;

        for (Usuario u : usuarios){
            if(u.email.equals(email)){
                return u;
            }
        }

        return null;
    }

    public Usuario buscarPorId(){
        return null;
    }

    public boolean inserir(Usuario usuario){
        return usuarios.add(usuario);
    }

    public boolean editar(Usuario usuario){
        return false;
    }

    public boolean excluir(Usuario usuario){
        return usuarios.remove(usuario);
    }
}
