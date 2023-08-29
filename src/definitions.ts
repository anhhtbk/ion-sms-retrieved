export interface AndroidSmsRetrievedPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
