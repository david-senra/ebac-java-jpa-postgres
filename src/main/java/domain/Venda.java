package domain;

import dao.ProdutoDao;
import factories.FactoryProdutos;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "tb_venda")
public class Venda implements IPersistente {

    public enum Status {
        INICIADA, CONCLUIDA, CANCELADA;

        public static Status getByName(String value) {
            for (Status status: Status.values()) {
                if (status.name().equals(value)) {
                    return status;
                }
            }
            return null;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "venda_seq")
    @SequenceGenerator(name="venda_seq", sequenceName = "sq_venda")
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true)
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "id_cliente_fk",
            foreignKey = @ForeignKey(name = "fk_venda_cliente"),
            referencedColumnName = "id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
    private Set<ProdutoQuantidade> produtos;

    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal;

    @Column(name = "data_venda", nullable = false)
    private Instant dataVenda;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_venda", nullable = false)
    private Status status;

    public Venda() {
        produtos = new HashSet<>();
    }

    public void adicionarProduto(Produto produto, Integer quantidade) {
        validarStatus();
        validarEstoque(produto, quantidade);
        Optional<ProdutoQuantidade> op =
                produtos.stream().filter(filter -> filter.getProduto().getId().equals(produto.getId())).findAny();
        if (op.isPresent()) {
            ProdutoQuantidade prodQtd = op.get();
            prodQtd.adicionar(quantidade);
        } else {
            FactoryProdutos.criarProdutoQuantidade(produto, this, quantidade);
        }
        reduzirEstoque(produto, quantidade);
        recalcularValorTotalVenda();
    }

    public void removerProduto(Produto produto, Integer quantidade) {
        validarStatus();
        Optional<ProdutoQuantidade> op =
                produtos.stream().filter(filter -> filter.getProduto().getId().equals(produto.getId())).findAny();

        if (op.isPresent()) {
            ProdutoQuantidade prodQtd = op.get();
            if (prodQtd.getQuantidade()>quantidade) prodQtd.remover(quantidade);
            else produtos.remove(op.get());
            aumentarEstoque(produto, quantidade);
            recalcularValorTotalVenda();
        }
    }

    private void validarStatus() {
        if (this.status == Status.CONCLUIDA) {
            throw new UnsupportedOperationException("IMPOSSÍVEL ALTERAR VENDA FINALIZADA");
        }
    }

    private void validarEstoque(Produto produto, Integer quantidade) {
        if (produto.getEstoque() <= quantidade) {
            throw new UnsupportedOperationException("ESTOQUE INSUFICIENTE");
        }
    }

    private void reduzirEstoque(Produto produto, Integer quantidade) {
        ProdutoDao produtoDao = new ProdutoDao();
        produto.reduzirEstoque(quantidade);
        produtoDao.alterar(produto);
    }

    private void aumentarEstoque(Produto produto, Integer quantidade) {
        ProdutoDao produtoDao = new ProdutoDao();
        produto.adicionarEstoque(quantidade);
        produtoDao.alterar(produto);
    }

    public void recalcularValorTotalVenda() {
        //validarStatus();
        BigDecimal valorTotal = BigDecimal.ZERO;
        for (ProdutoQuantidade produtoQuantidade : this.produtos) {
            valorTotal = valorTotal.add(produtoQuantidade.getValorTotal());
        }
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void removerTodosProdutos() {
        validarStatus();
        produtos.clear();
        valorTotal = BigDecimal.ZERO;
    }

    public Integer getQuantidadeTotalProdutos() {
        // Soma a quantidade getQuantidade() de todos os objetos ProdutoQuantidade
        return produtos.stream()
                .reduce(0, (partialCountResult, prod) -> partialCountResult + prod.getQuantidade(), Integer::sum);
    }

    public void setProdutos(Set<ProdutoQuantidade> produtos) {
        this.produtos = produtos;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Set<ProdutoQuantidade> getProdutos() {
        return produtos;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public Instant getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Instant dataTime) {
        this.dataVenda = dataTime;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void addProdutoQuantidade(ProdutoQuantidade prodqtd) {
        produtos.add(prodqtd);
    }

}
