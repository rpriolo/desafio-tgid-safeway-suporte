package view;

import controller.database.BancoDeDados;
import domain.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

	public static void main(String[] args) {
		BancoDeDados.gerarBancoDeDados();
	}

	public static void executar(List<Usuario> usuarios, List<Cliente> clientes, List<Empresa> empresas,
			List<Produto> produtos, List<Produto> carrinho, List<Venda> vendas) {
		Scanner sc = new Scanner(System.in);

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

				System.out.println("Escolha uma opção para iniciar");
				if (usuarioLogado.IsEmpresa()) {
					System.out.println("1 - Listar vendas");
					System.out.println("2 - Ver produtos");
					System.out.println("0 - Deslogar");
					Integer escolha = sc.nextInt();

					switch (escolha) {
					case 1: {
						System.out.println();
						System.out.println("************************************************************");
						System.out.println("VENDAS EFETUADAS");
						vendas.stream().forEach(venda -> {
							if (venda.getEmpresa().getId().equals(usuarioLogado.getEmpresa().getId())) {
								System.out.println("************************************************************");
								System.out.println("domain.Venda de código: " + venda.getCódigo() + " no CPF "
										+ venda.getCliente().getCpf() + ": ");
								venda.getItens().stream().forEach(x -> {
									System.out.println(x.getId() + " - " + x.getNome() + "    R$" + x.getPreco());
								});
								System.out.println("Total domain.Venda: R$" + venda.getValor());
								System.out.println("Total Taxa a ser paga: R$" + venda.getComissaoSistema());
								System.out.println("Total Líquido  para empresa"
										+ (venda.getValor() - venda.getComissaoSistema()));
								System.out.println("************************************************************");
							}

						});
						System.out.println("Saldo domain.Empresa: " + usuarioLogado.getEmpresa().getSaldo());
						System.out.println("************************************************************");

						executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
					}
					case 2: {
						System.out.println();
						System.out.println("************************************************************");
						System.out.println("MEUS PRODUTOS");
						produtos.stream().forEach(produto -> {
							if (produto.getEmpresa().getId().equals(usuarioLogado.getEmpresa().getId())) {
								System.out.println("************************************************************");
								System.out.println("Código: " + produto.getId());
								System.out.println("domain.Produto: " + produto.getNome());
								System.out.println("Quantidade em estoque: " + produto.getQuantidade());
								System.out.println("Valor: R$" + produto.getPreco());								
								System.out.println("************************************************************");
							}

						});
						System.out.println("Saldo domain.Empresa: " + usuarioLogado.getEmpresa().getSaldo());
						System.out.println("************************************************************");

						executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
					}
					case 0: {
						executar(usuarios, clientes, empresas, produtos, carrinho, vendas);

					}
					}

				} else {
					System.out.println("1 - Relizar Compras");
					System.out.println("2 - Ver Compras");
					System.out.println("0 - Deslogar");
					Integer escolha = sc.nextInt();
					switch (escolha) {
					case 1: {
						System.out.println("Para realizar uma compra, escolha a empresa onde deseja comprar: ");
						empresas.stream().forEach(x -> {
							System.out.println(x.getId() + " - " + x.getNome());
						});
						Integer escolhaEmpresa = sc.nextInt();
						Integer escolhaProduto = -1;
						do {
							System.out.println("Escolha os seus produtos: ");
							produtos.stream().forEach(x -> {
								if (x.getEmpresa().getId().equals(escolhaEmpresa)) {
									System.out.println(x.getId() + " - " + x.getNome());
								}
							});
							System.out.println("0 - Finalizar compra");
							escolhaProduto = sc.nextInt();
							for (Produto produtoSearch : produtos) {
								if (produtoSearch.getId().equals(escolhaProduto))
									carrinho.add(produtoSearch);
							}
						} while (escolhaProduto != 0);
						System.out.println("************************************************************");
						System.out.println("Resumo da compra: ");
						carrinho.stream().forEach(x -> {
							if (x.getEmpresa().getId().equals(escolhaEmpresa)) {
								System.out.println(x.getId() + " - " + x.getNome() + "    R$" + x.getPreco());
							}
						});
						Empresa empresaEscolhida = empresas.stream().filter(x -> x.getId().equals(escolhaEmpresa))
								.collect(Collectors.toList()).get(0);
						Cliente clienteLogado = clientes.stream()
								.filter(x -> x.getUsername().equals(usuarioLogado.getUsername()))
								.collect(Collectors.toList()).get(0);
						Venda venda = criarVenda(carrinho, empresaEscolhida, clienteLogado, vendas);
						System.out.println("Total: R$" + venda.getValor());
						System.out.println("************************************************************");
						carrinho.clear();
						executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
					}
					case 2: {
						System.out.println();
						System.out.println("************************************************************");
						System.out.println("COMPRAS EFETUADAS");
						vendas.stream().forEach(venda -> {
							if (venda.getCliente().getUsername().equals(usuarioLogado.getUsername())) {
								System.out.println("************************************************************");
								System.out.println("Compra de código: " + venda.getCódigo() + " na empresa "
										+ venda.getEmpresa().getNome() + ": ");
								venda.getItens().stream().forEach(x -> {
									System.out.println(x.getId() + " - " + x.getNome() + "    R$" + x.getPreco());
								});
								System.out.println("Total: R$" + venda.getValor());
								System.out.println("************************************************************");
							}

						});

						executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
					}
					case 0: {
						executar(usuarios, clientes, empresas, produtos, carrinho, vendas);

					}

					}
				}

			} else
				System.out.println("Senha incorreta");
		} else {
			System.out.println("Usuário não encontrado");
		}
	}

	public static Venda criarVenda(List<Produto> carrinho, Empresa empresa, Cliente cliente, List<Venda> vendas) {
		Double total = carrinho.stream().mapToDouble(Produto::getPreco).sum();
		Double comissaoSistema = total * empresa.getTaxa();
		int idVenda = vendas.isEmpty() ? 1 : vendas.get(vendas.size() - 1).getCódigo() + 1;
		Venda venda = new Venda(idVenda, carrinho.stream().toList(), total, comissaoSistema, empresa, cliente);
		empresa.setSaldo(empresa.getSaldo() + total);
		vendas.add(venda);
		return venda;
	}
}
