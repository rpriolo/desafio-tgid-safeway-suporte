package service;

import domain.Usuario;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class LoginService {
    private static Scanner sc = new Scanner(System.in);

    public static Usuario realizarLogin(List<Usuario> usuarios) {
        System.out.println("Entre com seu usuário e senha:");
        System.out.print("Usuário: ");
        String username = sc.next();
        System.out.print("Senha: ");
        String senha = sc.next();


        List<Usuario> usuariosSearch = usuarios.stream().filter(x -> x.getUsername().equals(username))
                .collect(Collectors.toList());
        if (usuariosSearch.size() > 0) {
            Usuario usuarioLogado = usuariosSearch.get(0);
            if ((usuarioLogado.getSenha().equals(senha))) {
                return usuarioLogado;
            } else {
                throw new RuntimeException("Senha inválida");
            }
        } else {
            throw new RuntimeException("Usuário inválido");
        }
    }
}
