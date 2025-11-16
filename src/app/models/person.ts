export type Genero = "MASCULINO" | "FEMENINO";  //exportamos las dos posibilidades del enum Genero como un string 

export interface Person {
    
    id?: number,
    nombre: string,
    apellido: string,
    dni: string,
    fechaNacimiento: string,     //lo pongo como un string porque aunque en mi back sea un LoacalDate, llega como un string
    telefono: string,
    direccion: string,
    genero: Genero,      
    email: string
    username: string
}
