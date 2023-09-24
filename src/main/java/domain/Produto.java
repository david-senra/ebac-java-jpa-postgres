package domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_produto")
public class Produto implements IPersistente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "produto_seq")
    @SequenceGenerator(name="produto_seq", sequenceName = "sq_produto")
    private Long id;

    @Column(name = "codigo", nullable = false, length = 10, unique = true)
    private String codigo;

    @Column(name = "nome", nullable = false, length = 20)
    private String nome;

    @Column(name = "descricao", nullable = false, length = 120)
    private String descricao;

    @Column(name = "preco", nullable = false)
    private BigDecimal preco;

    @Column(name = "estoque", columnDefinition = "integer default 0")
    private Integer estoque;

    public Produto() {

    }

    public Produto(Long id, String codigo, String nome, String descricao, BigDecimal preco, Integer estoque) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long codigo) {
        this.id = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    public void adicionarEstoque(Integer quantidade) {
        estoque += quantidade;
    }

    public void reduzirEstoque(Integer quantidade) {
        estoque -= quantidade;
    }
}
