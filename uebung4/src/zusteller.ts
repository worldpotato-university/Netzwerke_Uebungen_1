export class Zusteller {
  constructor(public readonly name: string) {
    this.status = ZustellerState.READY;
  }

  status: ZustellerState;
  startDelivery: number;
  stopDelivery: number;
  temperature: number;
}

export enum ZustellerState {
  READY,
  DELIVERING,
  DELIVERED,
}
