/*export type StrategyData = {// sino utilizamos una tabla en la DB borrar la estructura
    id: number;
    descripcion: string;
}

export default class Stragey {
    id: number;
    descripcion: string;

    constructor(strategy?: StrategyData){
        if(strategy){
            this.id = strategy.id;
            this.descripcion = strategy.descripcion;
        }else{
            this.id = 0;
            this.descripcion = "";
        }
    }
}*/

export  const Strategy = ["No Analizado","Distanciamiento Social","Cuarentena","Libre Circulación"];
