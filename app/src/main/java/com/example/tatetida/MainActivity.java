package com.example.tatetida;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Tamaño del tablero del juego
    private int grid_size;
    // Referencia al layout del tablero del juego
    TableLayout gameBoard;
    // Referencia al texto que muestra el turno actual
    TextView txt_turn;
    // Matriz que representa el estado del tablero del juego
    char [][] my_board;
    // Turno actual ('X' o 'O')
    char turn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar el tamaño del tablero a partir de un recurso de cadena
        grid_size = Integer.parseInt(getString(R.string.size_of_board));
        // Inicializar el tablero del juego como una matriz de caracteres
        my_board = new char [grid_size][grid_size];
        // Obtener referencias a los elementos de la interfaz de usuario
        gameBoard = (TableLayout) findViewById(R.id.mainBoard);
        txt_turn = (TextView) findViewById(R.id.turn);

        // Reiniciar el tablero y establecer el turno inicial
        resetBoard();
        // Actualizar el texto de la interfaz para mostrar el turno actual
        txt_turn.setText("Turno de "+turn);

        // Configurar los botones del tablero para que respondan a los clics del usuario
        for(int i = 0; i< gameBoard.getChildCount(); i++){
            TableRow row = (TableRow) gameBoard.getChildAt(i);
            for(int j = 0; j<row.getChildCount(); j++){
                TextView tv = (TextView) row.getChildAt(j);
                tv.setText(R.string.none); // Inicializar el texto del botón
                tv.setOnClickListener(Move(i, j, tv)); // Configurar el listener del botón
            }
        }

        // Configurar el botón de reinicio para que reinicie el juego
        Button reset_btn = (Button) findViewById(R.id.reset);
        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent current = getIntent();
                finish();
                startActivity(current);
            }
        });
    }

    // Método para reiniciar el tablero y establecer el turno inicial
    protected void resetBoard(){
        turn = 'X'; // El primer turno es de 'X'
        for(int i = 0; i< grid_size; i++){
            for(int j = 0; j< grid_size; j++){
                my_board[i][j] = ' '; // Inicializar todas las celdas como vacías
            }
        }
    }

    // Método para verificar el estado del juego
    protected int gameStatus(){
        // Verificar filas, columnas y diagonales para un ganador
        for(int i = 0; i< grid_size; i++){
            if(check_Row_Equality(i,'X'))
                return 1; // 'X' gana
            if(check_Column_Equality(i, 'X'))
                return 1; // 'X' gana
            if(check_Row_Equality(i,'O'))
                return 2; // 'O' gana
            if(check_Column_Equality(i,'O'))
                return 2; // 'O' gana
            if(check_Diagonal('X'))
                return 1; // 'X' gana
            if(check_Diagonal('O'))
                return 2; // 'O' gana
        }

        // Verificar si el tablero está completo (empate)
        boolean boardFull = true;
        for(int i = 0; i< grid_size; i++){
            for(int j = 0; j< grid_size; j++){
                if(my_board[i][j]==' ')
                    boardFull = false; // Hay al menos una celda vacía
            }
        }
        if(boardFull)
            return -1; // Empate
        else return 0; // Juego en curso
    }

    // Método para verificar si hay una diagonal ganadora
    protected boolean check_Diagonal(char player){
        int count_Equal1 = 0,count_Equal2 = 0;
        for(int i = 0; i< grid_size; i++)
            if(my_board[i][i]==player)
                count_Equal1++; // Contar las celdas iguales en la diagonal principal
        for(int i = 0; i< grid_size; i++)
            if(my_board[i][grid_size -1-i]==player)
                count_Equal2++; // Contar las celdas iguales en la diagonal secundaria
        if(count_Equal1== grid_size || count_Equal2== grid_size)
            return true; // Hay una diagonal ganadora
        else return false; // No hay una diagonal ganadora
    }

    // Método para verificar si hay una fila ganadora
    protected boolean check_Row_Equality(int r, char player){
        int count_Equal=0;
        for(int i = 0; i< grid_size; i++){
            if(my_board[r][i]==player)
                count_Equal++; // Contar las celdas iguales en la fila
        }

        if(count_Equal== grid_size)
            return true; // Hay una fila ganadora
        else
            return false; // No hay una fila ganadora
    }

    // Método para verificar si hay una columna ganadora
    protected boolean check_Column_Equality(int c, char player){
        int count_Equal=0;
        for(int i = 0; i< grid_size; i++){
            if(my_board[i][c]==player)
                count_Equal++; // Contar las celdas iguales en la columna
        }

        if(count_Equal== grid_size)
            return true; // Hay una columna ganadora
        else
            return false; // No hay una columna ganadora
    }

    // Método para verificar si una celda está vacía
    protected boolean Cell_Set(int r, int c){
        return !(my_board[r][c]==' '); // Retorna true si la celda no está vacía
    }

    // Método para detener el juego, deshabilitando los botones del tablero
    protected void stopMatch(){
        for(int i = 0; i< gameBoard.getChildCount(); i++){
            TableRow row = (TableRow) gameBoard.getChildAt(i);
            for(int j = 0; j<row.getChildCount(); j++){
                TextView tv = (TextView) row.getChildAt(j);
                tv.setOnClickListener(null); // Deshabilitar el listener del botón
            }
        }
    }

    // Método para manejar el movimiento de un jugador
    View.OnClickListener Move(final int r, final int c, final TextView tv){

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Cell_Set(r,c)) { // Verificar si la celda está vacía
                    my_board[r][c] = turn; // Marcar la celda con el turno actual
                    if (turn == 'X') {
                        tv.setText(R.string.X); // Actualizar el texto del botón
                        turn = 'O'; // Cambiar el turno al siguiente jugador
                    } else if (turn == 'O') {
                        tv.setText(R.string.O); // Actualizar el texto del botón
                        turn = 'X'; // Cambiar el turno al siguiente jugador
                    }
                    // Verificar el estado del juego después del movimiento
                    if (gameStatus() == 0) {
                        txt_turn.setText("Turno del jugador " + turn); // Actualizar el texto del turno
                    }
                    else if(gameStatus() == -1){
                        txt_turn.setText("Empate"); // Mostrar mensaje de empate
                        stopMatch(); // Detener el juego
                    }
                    else{
                        txt_turn.setText(turn+" Pierde!"); // Mostrar mensaje de ganador
                        stopMatch(); // Detener el juego
                    }
                }
                else{
                    txt_turn.setText(txt_turn.getText()+" Elegi una celda vacia"); // Mostrar mensaje de error
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
