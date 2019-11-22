export interface IRoutingResponse {
  response: {
    route: Array<{
      summary: {
        trafficTime: number
        travelTime: number,
      },
    }>,
  };
}
