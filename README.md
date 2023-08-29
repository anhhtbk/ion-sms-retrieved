# ion-sms-retrieved

Implement Android SMS retrived

## Install

```bash
npm install ion-sms-retrieved
npx cap sync
```

## API

<docgen-index>

* [`echo(...)`](#echo)
* [`unregisterSmsReceiver()`](#unregistersmsreceiver)
* [`startSmsUserConsent()`](#startsmsuserconsent)
* [`registerSmsReceiver()`](#registersmsreceiver)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### echo(...)

```typescript
echo(options: { value: string; }) => Promise<{ value: string; }>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ value: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### unregisterSmsReceiver()

```typescript
unregisterSmsReceiver() => void
```

--------------------


### startSmsUserConsent()

```typescript
startSmsUserConsent() => Promise<{ value: string; }>
```

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### registerSmsReceiver()

```typescript
registerSmsReceiver() => void
```

--------------------

</docgen-api>
