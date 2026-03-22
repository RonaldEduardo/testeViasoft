export enum Safra {
  INVERNO,
  VERAO,
  OUTONO,
  PRIMAVERA,
}

export const SafraLabels: { [key in Safra]: string } = {
  [Safra.INVERNO]: 'Inverno',
  [Safra.VERAO]: 'Verão',
  [Safra.OUTONO]: 'Outono',
  [Safra.PRIMAVERA]: 'Primavera',
};
