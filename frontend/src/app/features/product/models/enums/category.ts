export enum Category {
  FERTILIZANTE,
  SEMENTE,
  DEFENSIVO,
}

export const CategoryLabels: { [key in Category]: string } = {
  [Category.FERTILIZANTE]: 'Fertilizante',
  [Category.SEMENTE]: 'Semente',
  [Category.DEFENSIVO]: 'Defensivo',
};
