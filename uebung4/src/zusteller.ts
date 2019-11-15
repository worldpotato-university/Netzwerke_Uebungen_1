export class Zusteller {
  constructor(public readonly name: string) {
    this.status = ZustellerState.READY;
  }

  status: ZustellerState;
  startDelivery: number;
  stopDelivery: number;
  temperature: number;

  public async checkStatus(): Promise<void> {
    // TODO
  }
}

export enum ZustellerState {
  READY,
  DELIVERING,
  DELIVERED,
}
