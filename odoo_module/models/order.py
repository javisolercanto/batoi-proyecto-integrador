# -*- coding: utf-8 -*-
from openerp import models, fields, api

STATES = [
    ('0', 'PENDING'),
    ('1', 'READY'),
    ('2', 'OUT FOR DELIVERY'),
    ('3', 'DELIVERED'),
    ('4', 'NOT DELIVERED'),
]

class Order(models.Model):
    _name = 'batoi_logic.order'

    name = fields.Char(string='orderNumber', size=30, required=True)
    date = fields.Date('Request Date')
    status = fields.Selection(STATES, default=STATES[0][0], string="Order status")
    information = fields.Text('About the delivery')

    customer_id = fields.Many2one('batoi_logic.customer', 'Customer', required=True)
    lines = fields.One2many('batoi_logic.order_line', 'order_id', 'Lines', ondelete='cascade')
    address_id = fields.Many2one('batoi_logic.address', 'Address', required=True)
    
    delivery_notes = fields.One2many('batoi_logic.delivery_note','order_id', ondelete='cascade')

    _sql_constraints = [
        ('order_number_unique', 'unique("name")', 'Order Number must be unique!'),
    ]
