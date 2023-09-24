package domain;

import javax.persistence.*;

@Entity
@Table(name = "tb_cliente")
public class Cliente implements IPersistente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cliente_seq")
    @SequenceGenerator(name="cliente_seq", sequenceName = "sq_cliente")
    private Long id;

    @Column(name = "nome", nullable = false, length = 20)
    private String nome;

    @Column(name = "sobrenome", nullable = false, length = 30)
    private String sobrenome;

    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "telefone", nullable = false)
    private String telefone;

    @Column(name = "endereco", nullable = false, length = 100)
    private String endereco;

    @Column(name = "numero_endereco", nullable = false)
    private Long numeroEndereco;

    @Column(name = "cidade", nullable = false, length = 100)
    private String cidade;

    @Column(name = "estado", nullable = false, length = 2)
    private String estado;

    public Cliente() {

    }

    public Cliente(Long id, String nome, String sobrenome, String cpf, String telefone, String endereco, Long numero, String cidade, String estado) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.endereco = endereco;
        this.cidade = cidade;
        this.estado = estado;
        this.numeroEndereco = numero;
    }

    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public Long getNumeroEndereco() {
        return numeroEndereco;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setNumeroEndereco(Long numeroEndereco) {
        this.numeroEndereco = numeroEndereco;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void adicionarProduto(Produto produto) {

    }

    public Produto buscarNoCarrinho(Produto produto) {
        return produto;
    }

    public void removerProduto(Produto produto) {

    }

    public void limparCarrinho() {

    }

    public void adicionarPedido() {

    }

}
