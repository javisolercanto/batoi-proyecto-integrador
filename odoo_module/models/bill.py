# -*- coding: utf-8 -*-
from openerp import models, fields, api

class bill(models.Model):
    _name = 'batoi_logic.bill'

    date = fields.Date('Creation Date')
    address_id = fields.Many2one('batoi_logic.address', 'Associated Address', required=True)
    deliverynote_id = fields.Many2one('batoi_logic.delivery_note', 'Delivery note', required=True)

    # One2one -->
    _sql_constraints = [
        ('one2One_bill', 'unique("deliverynote_id")',
         'Delivery Note must be unique, because is a One2one relationship!'),
    ]
