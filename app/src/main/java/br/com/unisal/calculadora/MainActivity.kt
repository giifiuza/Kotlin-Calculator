package br.com.unisal.calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.unisal.calculadora.ui.theme.CalculadoraTheme
import br.com.unisal.calculadora.ui.theme.RoxoTecla
import br.com.unisal.calculadora.ui.theme.VermelhoTecla
import br.com.unisal.calculadora.ui.theme.white100
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan


class MainActivity : ComponentActivity() {
    var visor by mutableStateOf("0")
    val pilhaOperador = mutableListOf<String>()
    val pilhaOperando = mutableListOf<String>()

    var aguardandoOperando = false

    val VALOR_PI = 3.14

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    createCalculator(visor, Modifier.padding(innerPadding))
                }
            }
        }
    }

    @Composable
    fun createCalculator(visor: String, modifier: Modifier = Modifier) {
        Column(
            modifier = modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                fontSize = 32.sp,
                text = visor
            )
            Spacer(modifier = Modifier.height(8.dp))
            // linha das funcoes trigonometricas
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                createSmallButton("Sin", OperationButton.SENO)
                createSmallButton("Cos", OperationButton.COSSENO)
                createSmallButton("Tan", OperationButton.TANGENTE)
                createSmallButton("!", OperationButton.FATORIAL)
            }
            Spacer(modifier = Modifier.height(8.dp))
            // linha das funcoes cientificas
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                createSmallButton("Pi", OperationButton.PI)
                createSmallButton("Inv", OperationButton.INVERSO)
                createSmallButton("Sqrt", OperationButton.RAIZ)
                createSmallButton("^", OperationButton.POTENCIA)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                createSmallButton("%", OperationButton.PERCENTUAL)
                createSmallButton("/", OperationButton.DIVISAO)
                createSmallButton("*", OperationButton.MULTIPLICACAO)
                createSmallButton("-", OperationButton.SUBTRACAO)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                createSmallButton("7", OperationButton.SETE)
                createSmallButton("8", OperationButton.OITO)
                createSmallButton("9", OperationButton.NOVE)
                createSmallButton("+", OperationButton.SOMA)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                createSmallButton("4", OperationButton.QUATRO)
                createSmallButton("5", OperationButton.CINCO)
                createSmallButton("6", OperationButton.SEIS)
                createSmallButton(".", OperationButton.VIRGULA)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                createSmallButton("1", OperationButton.UM)
                createSmallButton("2", OperationButton.DOIS)
                createSmallButton("3", OperationButton.TRES)
                createSmallButton("=", OperationButton.IGUALDADE)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                createSmallButton("+/-", OperationButton.TROCA_SINAL)
                createSmallButton("0", OperationButton.ZERO)
                createSmallButton("C", OperationButton.LIMPAR)
                createSmallButton("<-", OperationButton.APAGAR)
            }
        }
    }

    @Composable
    fun createSmallButton(texto: String, id: OperationButton) {
        Button(
            modifier = Modifier.width(80.dp).height(48.dp),
            onClick = {
                if (id.ordinal <= OperationButton.VIRGULA.ordinal) {
                    numPress(id)
                } else {
                    opPress(id)
                }
            },
            colors = if (id == OperationButton.APAGAR) {
                ButtonDefaults.buttonColors(
                    containerColor = VermelhoTecla,
                    contentColor = white100
                )
            } else {
                ButtonDefaults.buttonColors(
                    containerColor = RoxoTecla,
                    contentColor = white100
                )
            }
        ) {
            Text(texto)
        }
    }

    fun numPress(id: OperationButton) {
        // se o visor esta mostrando um erro ou um resultado, comeca um numero novo
        if (aguardandoOperando || visor == ERRO) {
            visor = "0"
            aguardandoOperando = false
        }

        if ((visor == "0") && (id.name == OperationButton.ZERO.name)) {
            return
        }

        if ((visor.contains(".")) && (id.name == OperationButton.VIRGULA.name)) {
            return
        }

        var tmp = id.name
        if (tmp == OperationButton.VIRGULA.name) {
            tmp = "."
        } else {
            tmp = id.ordinal.toString()
        }

        if (visor.length == 1 && visor == "0" && tmp != ".") {
            visor = tmp
        } else {
            visor += tmp
        }

    }

    fun opPress(id: OperationButton) {
        if (id == OperationButton.LIMPAR) {
            pilhaOperador.clear()
            pilhaOperando.clear()
            aguardandoOperando = false
            visor = "0"
            return
        }

        if (id == OperationButton.APAGAR) {
            apagar()
            return
        }

        if (id == OperationButton.IGUALDADE) {
            igualdade()
            return
        }

        // operacoes unarias trabalham direto com o valor do visor
        if (ehOperacaoUnaria(id)) {
            operacaoUnaria(id)
            return
        }

        // se o visor esta com erro nao deixa empilhar operacao
        if (visor == ERRO) {
            return
        }

        // usuario trocou de operador antes de digitar o proximo operando
        if (aguardandoOperando && pilhaOperador.isNotEmpty()) {
            pilhaOperador[pilhaOperador.lastIndex] = id.name
            return
        }

        if (pilhaOperador.isEmpty()) {
            pilhaOperador.add(id.name)
            pilhaOperando.add(visor)
        } else {
            //executar a operacao
            igualdade()
            if (visor == ERRO) {
                return
            }
            pilhaOperador.add(id.name)
            pilhaOperando.add(visor)
        }

        aguardandoOperando = true
    }

    fun igualdade() {
        if (pilhaOperador.isEmpty() || pilhaOperando.isEmpty()) {
            return
        }

        val operador = pilhaOperador.removeAt(pilhaOperador.lastIndex)
        val operando = pilhaOperando.removeAt(pilhaOperando.lastIndex).toDoubleOrNull()
        val aux = visor.toDoubleOrNull()

        // se algum dos valores nao for um numero valido mostra erro
        if (operando == null || aux == null) {
            mostraErro()
            return
        }

        //implementar as operações
        if (operador == OperationButton.SOMA.name) {
            visor = formata(operando + aux)
        } else if (operador == OperationButton.SUBTRACAO.name) {
            visor = formata(operando - aux)
        } else if (operador == OperationButton.MULTIPLICACAO.name) {
            visor = formata(operando * aux)
        } else if (operador == OperationButton.DIVISAO.name) {
            if (aux == 0.0) {
                // divisao por zero
                mostraErro()
                return
            }
            visor = formata(operando / aux)
        } else if (operador == OperationButton.POTENCIA.name) {
            visor = formata(operando.pow(aux))
        }

        aguardandoOperando = true
    }

    // diz se a tecla e uma operacao que usa somente o valor do visor
    fun ehOperacaoUnaria(id: OperationButton): Boolean {
        return id == OperationButton.SENO ||
                id == OperationButton.COSSENO ||
                id == OperationButton.TANGENTE ||
                id == OperationButton.FATORIAL ||
                id == OperationButton.PI ||
                id == OperationButton.INVERSO ||
                id == OperationButton.RAIZ ||
                id == OperationButton.PERCENTUAL ||
                id == OperationButton.TROCA_SINAL
    }

    fun operacaoUnaria(id: OperationButton) {
        // PI so escreve a constante no visor, nao precisa ler o valor atual
        if (id == OperationButton.PI) {
            visor = formata(VALOR_PI)
            aguardandoOperando = false
            return
        }

        val valor = visor.toDoubleOrNull()
        if (valor == null) {
            mostraErro()
            return
        }

        if (id == OperationButton.SENO) {
            // o angulo e usado em radianos (Pi = 3.14 equivale a 180 graus)
            visor = formata(sin(valor))
        } else if (id == OperationButton.COSSENO) {
            visor = formata(cos(valor))
        } else if (id == OperationButton.TANGENTE) {
            visor = formata(tan(valor))
        } else if (id == OperationButton.RAIZ) {
            if (valor < 0.0) {
                // raiz quadrada de numero negativo
                mostraErro()
                return
            }
            visor = formata(sqrt(valor))
        } else if (id == OperationButton.INVERSO) {
            if (valor == 0.0) {
                // inverso de zero
                mostraErro()
                return
            }
            visor = formata(1.0 / valor)
        } else if (id == OperationButton.FATORIAL) {
            visor = fatorial(valor)
        } else if (id == OperationButton.PERCENTUAL) {
            visor = formata(valor / 100.0)
        } else if (id == OperationButton.TROCA_SINAL) {
            visor = formata(valor * -1.0)
        }

        // depois de uma operacao unaria o resultado continua no visor
        // e pode ser usado em uma operacao binaria
        aguardandoOperando = false
    }

    // calcula o fatorial e trata os valores incompativeis
    fun fatorial(valor: Double): String {
        // fatorial so existe para inteiros positivos (ou zero)
        if (valor < 0.0 || valor != Math.floor(valor)) {
            return ERRO
        }

        // acima de 20 o resultado nao cabe em um Long
        if (valor > 20.0) {
            return ERRO
        }

        var resultado = 1L
        var i = 2L
        while (i <= valor.toLong()) {
            resultado *= i
            i++
        }
        return resultado.toString()
    }

    // apaga o ultimo digito digitado
    fun apagar() {
        if (aguardandoOperando || visor == ERRO) {
            visor = "0"
            aguardandoOperando = false
            return
        }

        if (visor.length <= 1) {
            visor = "0"
        } else {
            visor = visor.substring(0, visor.length - 1)
            if (visor == "-") {
                visor = "0"
            }
        }
    }

    // limpa as pilhas e avisa o usuario que a operacao nao pode ser feita
    fun mostraErro() {
        pilhaOperador.clear()
        pilhaOperando.clear()
        aguardandoOperando = false
        visor = ERRO
    }

    // deixa o numero mais bonito no visor (tira o .0 dos inteiros)
    fun formata(valor: Double): String {
        if (valor.isNaN() || valor.isInfinite()) {
            return ERRO
        }

        if (abs(valor) < 1e15 && valor == Math.floor(valor)) {
            return valor.toLong().toString()
        }

        return valor.toString()
    }

    enum class OperationButton {
        ZERO,
        UM,
        DOIS,
        TRES,
        QUATRO,
        CINCO,
        SEIS,
        SETE,
        OITO,
        NOVE,
        VIRGULA,
        SOMA,
        SUBTRACAO,
        MULTIPLICACAO,
        DIVISAO,
        POTENCIA,
        PERCENTUAL,
        SENO,
        COSSENO,
        TANGENTE,
        FATORIAL,
        PI,
        INVERSO,
        RAIZ,
        TROCA_SINAL,
        APAGAR,
        IGUALDADE,
        LIMPAR
    }

    @Composable
    fun SeletorDeTemas(temaSelecionado : MaterialTheme,
                       onTemaselecionado: (MaterialTheme) -> Unit){

    }

    companion object {
        const val ERRO = "Erro"
    }
}
