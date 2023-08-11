package com.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Scanner;

import com.example.db.DB;
import com.example.db.DbException;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        System.out.println("Hello World!");
        Connection conn = null;
        Statement st = null;
        Scanner sc = new Scanner(System.in);
        try {
            conn = DB.gConnection();
            st = conn.createStatement();
            createTable(st);
            int op;
            do {
                System.out.println("CRUD DE PRODUTOS: ");
                System.out.println("1 - Cadastrar ");
                System.out.println("2 - Apagar ");
                System.out.println("3 - Listar ");
                System.out.println("4 - Atualizar ");
                System.out.println("0 - Sair ");
                op = sc.nextInt();
                sc.nextLine();
                switch (op) {
                    case 1:
                        insert(st, sc, op);
                        break;
                    case 2:
                        delete(st, sc);
                        break;
                    case 3:
                        list(st);
                        break;
                    case 0:
                        break;
                }
            } while (op != 0);
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            try {
                sc.close();
                st.close();
                conn.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    private static void list(Statement st) throws SQLException {
        System.out.println("Listar todos produtos: ");
        System.out.println("ID PRODUTO  -  NOME PRODUTO");
        String pgSqlSelect = "SELECT * FROM tb_produtos;";
        ResultSet rs = st.executeQuery(pgSqlSelect);
        while (rs.next()) {
            System.out.println(
                    rs.getInt("id")
                            + " - "
                            + rs.getString("nome"));
        }
    }

    private static void delete(Statement st, Scanner sc) throws SQLException {
        System.out.println("Apagar produto por ID");
        System.out.print("Digite o id do produto que deseja apagar: ");
        Integer idDeletar = sc.nextInt();
        sc.nextLine();
        String pgSqlDelete = "DELETE FROM tb_produtos WHERE tb_produtos.id = " + idDeletar + ";";
        st.executeUpdate(pgSqlDelete);
    }

    private static void insert(Statement st, Scanner sc, int op) throws SQLException {
        System.out.println("Cadastrar novo Produto:");
        System.out.print("Digite o nome do produto: ");
        String nome = sc.nextLine();
        System.out.print("Digite a descricao do produto: ");
        String descricao = sc.nextLine();
        System.out.print("Digite a categoria do produto: ");
        String categoria = sc.nextLine();
        System.out.print("Digite o preco do produto: ");
        Double preco = sc.nextDouble();
        sc.nextLine();
        System.out.print("Digite o estoque do produto: ");
        Integer estoque = sc.nextInt();
        sc.nextLine();

        String pgSqlInsert = "INSERT INTO tb_produtos (nome, descricao, categoria, preco, estoque)"
                + " VALUES ("
                + " '" + nome + "', "
                + " '" + descricao + "', "
                + " '" + categoria + "', "
                + " " + String.format("%.2f", preco) + ", "
                + " " + estoque + ""
                + ");";
        st.executeUpdate(pgSqlInsert);
    }

    private static void createTable(Statement st) throws SQLException {
        String tableName = "tb_produtos";
        String pgsqlCreate = "CREATE TABLE IF NOT EXISTS " +
                tableName +
                " (" +
                "id SERIAL, " +
                "nome VARCHAR(250), " +
                "descricao VARCHAR(250), " +
                "categoria VARCHAR(250), " +
                "preco NUMERIC, " +
                "estoque INTEGER" +
                ");";
        st.execute(pgsqlCreate);
    }

}
