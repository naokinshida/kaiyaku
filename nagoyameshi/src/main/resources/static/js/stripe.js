const express = require('express');
const bodyParser = require('body-parser');
const stripe = require('stripe')('sk_test_51PhnYHENjJ7uZX1aJ6hA06wzRl2SVkcjsAJbx0DQSkgMRVx5Cs9TbWkHgdES1cMSzQTeTltwEEfaLF9geuY5Ne4v00HQdEC6iE');

const app = express();
const endpointSecret = 'ウェブフック検証用のシークレット（オプション）';

app.use(bodyParser.raw({ type: 'application/json' }));

app.post('/webhook', (req, res) => {
  const sig = req.headers['stripe-signature'];

  let event;

  try {
    event = stripe.webhooks.constructEvent(req.body, sig, endpointSecret);
  } catch (err) {
    res.status(400).send(`Webhook Error: ${err.message}`);
    return;
  }

  switch (event.type) {
    case 'invoice.payment_succeeded':
      const invoice = event.data.object;
      // 支払い成功時の処理を実装
      console.log('Payment succeeded for invoice:', invoice.id);
      break;
    case 'invoice.payment_failed':
      const paymentIntent = event.data.object;
      // 支払い失敗時の処理を実装
      console.log('Payment failed for invoice:', paymentIntent.id);
      break;
    case 'customer.subscription.deleted':
      const subscription = event.data.object;
      // サブスクリプションがキャンセルされた時の処理を実装
      console.log('Subscription deleted:', subscription.id);
      break;
    // 他のイベントも必要に応じて処理
    default:
      console.log(`Unhandled event type ${event.type}`);
  }

  res.sendStatus(200);
});

app.listen(3000, () => console.log('Running on port 3000'));
