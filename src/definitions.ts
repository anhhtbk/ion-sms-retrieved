export interface AndroidSmsRetrievedPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  unregisterSmsReceiver(): void;
  startSmsUserConsent(): Promise<{ value: string }>;
  registerSmsReceiver(): void;
}
